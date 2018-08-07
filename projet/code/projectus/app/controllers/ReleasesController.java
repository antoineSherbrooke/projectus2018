package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import Security.ProductOwner;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;
import org.apache.commons.lang3.time.DateUtils;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.release;
import views.html.release_dial;
import views.html.infos_release;
import views.html.release_solo;

import javax.inject.Inject;

@With(Secure.class)
public class ReleasesController extends Controller {

    @Inject
    FormFactory formFactory;

    private final static DateFormat format = new SimpleDateFormat("EEEE, d MMMM, y", Locale.ENGLISH);

    public Result index(){
        endReleaseIfPasted();
        endSprintIfFinished();

        List<Releases> releases = Ebean.find(Releases.class).where()
                .eq("projects_id",SessionController.getProjectId()).orderBy("release_date").findList();
        List<Releases> releasesActive = getReleaseActive(releases);
        Releases releasesInRunning = Releases.checkRunning(releasesActive);


        return ok(release.render(releasesActive, getReleaseDisactive(releases),  getSprintStartable(releasesInRunning)));
    }

    public Result releases(){
        List<Releases> releases = Ebean.find(Releases.class).where()
                .eq("projects_id",SessionController.getProjectId()).orderBy("release_date").findList();
        List<Releases> releasesActive = getReleaseActive(releases);
        Releases releasesInRunning = Releases.checkRunning(releasesActive);

        return ok(release_solo.render(releasesActive, getReleaseDisactive(releases), getSprintStartable(releasesInRunning)));
    }

    public static void endSprintIfFinished() {
        Projects projectsRunning = SessionController.getProjectRunning();
        for (Releases releasesLoop : projectsRunning.getReleases()) {
            for (Sprint sprintLoop : releasesLoop.getSprint()) {
                if (sprintLoop.getEndDate().before(new Date()) && sprintLoop.getStartDate().before(new Date()) && sprintLoop.getState() == Sprint.State.RUNNING) {
                    sprintLoop.cancelRemainingDoingTasks();
                    /** TODO
                     *  Associed task not finish to next sprint
                     */
                    sprintLoop.setState(Sprint.State.FINISHED);
                    sprintLoop.update();
                }
            }
        }
    }

    public static void endReleaseIfPasted() {

        Projects projectRunning = SessionController.getProjectRunning();
        Date now = new Date();
        Date dateClosestToNow = DateUtils.addMonths(now, 100);
        int idRunningRelease = 0;

        for (Releases releasesLoop : projectRunning.getReleases()) {

            if (releasesLoop.getReleaseDate().before(now)) {
                endSprintIfFinished();
            } else if (dateClosestToNow.after(releasesLoop.getReleaseDate())) {
                idRunningRelease = releasesLoop.getId();
                dateClosestToNow = releasesLoop.getReleaseDate();
            }

            releasesLoop.setRunning(false);
            releasesLoop.update();
        }

        try {
            Releases nextRelease = Releases.find.byId(idRunningRelease);
            nextRelease.setRunning(true);
            nextRelease.update();
        } catch (Exception ignored) {}

    }

    private List<Releases> getReleaseDisactive(List<Releases> releases){
        return releases.stream().filter(releases1 -> !releases1.getActive()).collect(Collectors.toList());
    }

    public int getRunningId(Releases releasesInRunning){
        return releasesInRunning == null ? -1 :releasesInRunning.getId();
    }

    private List<Releases> getReleaseActive(List<Releases> releases){
        return releases.stream().filter(Releases::getActive).collect(Collectors.toList());
    }

    @With(ProductOwner.class)
    public Result addRelease(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String name = requestData.get("releaseName");
        String comment = requestData.get("releaseDescription");
        String releaseDate = requestData.get("releaseEndDate");

		Projects runningProject = Ebean.find(Projects.class).where().eq("id",SessionController.getProjectId()).findUnique();
        if (runningProject == null) {
            throw new NullPointerException("Invalid id project");
        }
		Date endDate;
		try {
			endDate = format.parse(releaseDate);
		}catch (Exception e) {
			return badRequest("The date is incorrect");
		}
		if(name.isEmpty() || comment.isEmpty()){
			return badRequest("Fields are required");
		}
        Releases release = new Releases(name, endDate, comment, false, runningProject);
        if(Releases.find.where().eq("running", true).findRowCount() == 0){
            release.setRunning(true);
        }
        Releases runningRelease = Releases.find.where().and(Expr.eq("running", true), Expr.eq("projects_id", runningProject.getId())).findUnique();
        if(getReleaseAfter(endDate) != null){
            for(Sprint sprint : getReleaseAfter(endDate).getSprint()){
                if(sprint.getStartDate().before(endDate)){
                    return badRequest("You can't add this release. A release that contains sprints before this date already exist !");
                }
            }
        }
        if(runningRelease != null){
            if(release.getReleaseDate().before(runningRelease.getReleaseDate())){
                if(runningRelease.getSprint().size() == 0){
                    release.setRunning(true);
                    runningRelease.setRunning(false);
                    runningRelease.update();
                }else{
                    return badRequest("You can't add this release. A running release that contains sprints already exist after the dates you entered !");
                }
            }
        }else {
            release.setRunning(true);
        }
		release.save();
        History.newRelease(release);
		return releases();
	}

    @With(ProductOwner.class)
	public Result dialDisableRelease(Integer id){
		Releases releases = Releases.find.byId(id);
		return ok(release_dial.render(releases));
	}

    @With(ProductOwner.class)
    private int getSprintStartable(Releases releasesInRunning){
        if(SessionController.authorization(Members.EnumMem.SCRUM_MASTER) && releasesInRunning != null){
            Date now = new Date();
            List<Sprint> sprints = releasesInRunning.getSprint().stream()
                    .filter(Sprint::isInactive).filter(sprint -> sprint.getEndDate().after(now)).collect(Collectors.toList());

            Collections.sort(sprints, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));

            return sprints.isEmpty() ? -1 : sprints.get(0).getId();
        }
        return -1;
    }

    @With(ProductOwner.class)
    private Releases getReleaseAfter(Date date){
        List<Releases> releases = Releases.find.where().and(Expr.eq("active", true), Expr.eq("projects_id", SessionController.getProjectRunning().getId())).orderBy("release_date").findList();
        for(Releases release : releases){
            if(release.getReleaseDate().after(date)){
                return release;
            }
        }
        return null;
    }

    @With(ProductOwner.class)
    public Result disableRelease(Integer id){
        //Releases release = Ebean.find(Releases.class).where().eq("id",id).findUnique();
        Releases release = Releases.find.byId(id);
        if(release.getSprint().size() != 0){
            return badRequest("You can't disable a release that contains sprint.");
        }
        if(release.getRunning()){
            Releases newReleaseRunning = null;
            List<Releases> activeReleases = Ebean.find(Releases.class).where().eq("active",true).orderBy("release_date").findList();
            if(activeReleases.size() > 1){
                newReleaseRunning = activeReleases.get(1);
            }
            if(newReleaseRunning != null){
                newReleaseRunning.setRunning(true);
                newReleaseRunning.update();
            }else {
            }
        }
        release.setActive(false);
        release.setRunning(false);
        release.update();
        History.disableRelease(release);
		return releases();
	}

    @With(ProductOwner.class)
    public Result activateRelease(Integer id){
		//Releases release = Ebean.find(Releases.class).where().eq("id",id).findUnique();
        Releases release = Releases.find.byId(id);
        if(getReleaseAfter(release.getReleaseDate()) != null){
            for(Sprint sprint : getReleaseAfter(release.getReleaseDate()).getSprint()){
                if(sprint.getStartDate().before(release.getReleaseDate())){
                    return badRequest("You can't activate this release. A release that contains sprints before this date already exist !");
                }
            }
        }
        if(Releases.find.where().eq("running", true).findRowCount() == 0){
            release.setRunning(true);
        }
        Releases runningRelease = Releases.find.where().and(Expr.eq("running", true), Expr.eq("projects_id", SessionController.getProjectRunning().getId())).findUnique();

        if(runningRelease != null){
            if(release.getReleaseDate().before(runningRelease.getReleaseDate())){
                if(runningRelease.getSprint().size() == 0){
                    release.setRunning(true);
                    runningRelease.setRunning(false);
                    runningRelease.update();
                }else{
                    return badRequest("You can't activate this release. A running release that contains sprints already exist after the dates you entered !");
                }
            }
        }else {
            release.setRunning(true);
        }
		release.setActive(true);
		release.update();
        History.activateRelease(release);
		return releases();
	}

	public Result infosRelease(Integer releaseId){
		Releases release = Ebean.find(Releases.class).where().eq("id",releaseId).findUnique();
		return ok(infos_release.render(release));
	}

    @With(ProductOwner.class)
    public Date getAvailableMinDate(Releases release){
        List<Releases> activeReleasesSortByDatesDESC = Releases.find.where().and(Expr.eq("active", true), Expr.eq("projects_id", SessionController.getProjectRunning().getId())).orderBy("release_date desc").findList();
        Releases beforeRelease = null;
        Date dateReturn = null;
        for(Releases r : activeReleasesSortByDatesDESC){
            if(r.getReleaseDate().before(release.getReleaseDate())){
                beforeRelease = r;
                break;
            }
        }
        List<Sprint> sprintsSorted = release.getSprintSorted();
        Date lastSprintDate = null;
        if(!sprintsSorted.isEmpty()){
            lastSprintDate = sprintsSorted.get(sprintsSorted.size() - 1).getEndDate();
            dateReturn = lastSprintDate;
        }else {
            if(beforeRelease != null){
                dateReturn = new Date(beforeRelease.getReleaseDate().getTime() + (1000 * 60 * 60 * 24));
            }
        }
        return dateReturn;
    }

    @With(ProductOwner.class)
    public Date getAvailableMaxDate(Releases release){
        List<Releases> activeReleasesSortByDatesASC = Releases.find.where().and(Expr.eq("active", true), Expr.eq("projects_id", SessionController.getProjectRunning().getId())).orderBy("release_date").findList();
        Releases nextRelease = null;
        Date dateReturn = null;
        for(Releases r : activeReleasesSortByDatesASC){
            if(r.getReleaseDate().after(release.getReleaseDate())){
                nextRelease = r;
                break;
            }
        }
        if(nextRelease != null){
            List<Sprint> sprintsSortedNextRelease = nextRelease.getSprintSorted();
            if(!sprintsSortedNextRelease.isEmpty()){
                dateReturn = sprintsSortedNextRelease.get(0).getStartDate();
            }else {
                dateReturn = new Date(nextRelease.getReleaseDate().getTime() - (1000 * 60 * 60 * 24));
            }
        }
        return dateReturn;
    }

    @With(ProductOwner.class)
    public Result infoReleaseEdit(Integer id){
        ObjectNode result = Json.newObject();
        Releases release = Ebean.find(Releases.class).where().eq("id",id).findUnique();

        if(getAvailableMinDate(release) != null){
            result.put("minDate", format.format(getAvailableMinDate(release)));
        }else {
            result.put("minDate", 0);
        }

        if(getAvailableMaxDate(release) != null){
            result.put("maxDate", format.format(getAvailableMaxDate(release)));
        }else {
            result.put("maxDate", 0);
        }

        result.put("id",release.getId());
        result.put("name",release.getName());
        try{
            result.put("date", format.format(release.getReleaseDate()));
        }catch (Exception e) {
            return badRequest("Wrong date");
        }
        result.put("comment",release.getComment());
        result.put("running",release.getRunning());
        result.put("activate",release.getActive());
        return ok(result);
    }

    @With(ProductOwner.class)
    public Result editRelease(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int id = Integer.parseInt(requestData.get("releaseId"));
        String name = requestData.get("releaseName");
        String comment = requestData.get("releaseDescription");
        String date = requestData.get("releaseDate");

        Releases release = Ebean.find(Releases.class).where().eq("id",id).findUnique();
        if(release.checkSprintRunning()){
            return badRequest("You can't edit this release !");
        }
        release.setComment(comment);
        release.setName(name);
        try{
            release.setReleaseDate(format.parse(date));
        }catch (Exception e) {
            return badRequest("Wrong date");
        }

        release.update();
        return releases();
    }
}

