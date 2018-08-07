package controllers;

import Security.ScrumMaster;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Dates;
import views.html.sprint;
import views.html.sprint_solo;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@With(Secure.class)
public class SprintsController extends Controller {

    @Inject
    FormFactory formFactory;

    private final static DateFormat format = new SimpleDateFormat("EEEE, d MMMM, y", Locale.ENGLISH);
    private final static DateFormat format2 = new SimpleDateFormat("EEEE MMMM d H:m:s z y", Locale.ENGLISH);

    public Result index(Integer sprintId){
        Sprint sprintToRender = Sprint.find.byId(sprintId);
        if(sprintToRender != null){
            Projects project = sprintToRender.getReleases().getProjects();
            if(project.getId() != SessionController.getProjectId()){
                return redirect(routes.ReleasesController.index());
            }
        }else {
            return redirect(routes.ReleasesController.index());
        }
        return ok(sprint.render(sprintToRender));
	}

    public Result sprint(Integer sprintId){
        Sprint sprintToRender = Sprint.find.byId(sprintId);
        if(sprintToRender != null){
            Projects project = sprintToRender.getReleases().getProjects();
            if(project.getId() != SessionController.getProjectId()){
                return redirect(routes.ReleasesController.index());
            }
        }else {
            return redirect(routes.ReleasesController.index());
        }
        return ok(sprint_solo.render(sprintToRender));
    }


    @With(ScrumMaster.class)
    public Result addSprint(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int idRelease = Integer.parseInt(requestData.get("sprintRelease"));
        String name = requestData.get("sprintName");
        String begin = requestData.get("sprintBeginDate");
        String end = requestData.get("sprintEndDate");
        String description = requestData.get("sprintDescription");
        Releases release = Releases.find.byId(idRelease);

		Date beginDate ;
		Date endDate ;
		try {
			beginDate = format.parse(begin);
			endDate = format.parse(end);

		} catch (Exception e) {
			return badRequest("The dates are incorrect");
		}
		if(name.isEmpty() || description.isEmpty()){
			return badRequest("Fields are required");
		}
		if(beginDate.after(endDate)){
			return badRequest("The dates are incorrect !");
		}
        if(endDate.after(release.getReleaseDate()) || beginDate.after(release.getReleaseDate())){
            return badRequest("Sprint dates out of release Dates !");
        }

		Sprint sprint = new Sprint(name, beginDate, endDate, release, description);
		sprint.save();

        History.newSprint(sprint);
		return new ReleasesController().releases();
	}

    @With(ScrumMaster.class)
    public Result abortSprint(Integer sprintId) {
        return setSprintState(sprintId, Sprint.State.ABORTED);
    }

    @With(ScrumMaster.class)
    public Result startSprint(Integer sprintId) {

        return setSprintState(sprintId, Sprint.State.RUNNING);
    }

    public static Result setSprintState(Integer sprintId, Sprint.State newState) {
        Sprint sprint = Sprint.find.byId(sprintId);
        Releases release = sprint.getReleases();
        if(newState == Sprint.State.RUNNING && release.hasActiveSprint()) {
            return badRequest("A sprint is already running !");
        }else if(newState == Sprint.State.RUNNING && !sprint.isNotEmpty()) {
            return badRequest("This sprint have no tasks assigned !");
        } else if (newState == Sprint.State.RUNNING) {
            String sprintName = sprint.getName();
            Projects runningProject = SessionController.getProjectRunning();
            List<Meetings> defaultMeetings = new ArrayList<>();
            defaultMeetings.add(new Meetings(sprintName+": End Sprint Meeting",
                    "Meeting at the end of the sprint ("+sprint.getName()+")", 60, false, runningProject, sprint));
            defaultMeetings.add(new Meetings(sprintName+": Retrospective Meeting",
                    "Meeting to discuss on what could improve", 60, false, runningProject, sprint));
            defaultMeetings.add(new Meetings(sprintName+": Next Sprint Meeting",
                    "Meeting for the next Sprint", 60, false, runningProject, sprint));
            Ebean.saveAll(defaultMeetings);
        }

        if(newState == Sprint.State.FINISHED){
            sprint.setEndDate(new Date());
            sprint.cancelRemainingDoingTasks();
        }
        if(newState == Sprint.State.RUNNING){
            sprint.setStartDate(new Date());
        }
        if(newState == Sprint.State.ABORTED){
            sprint.setEndDate(new Date());
            sprint.cancelRemainingDoingTasks();
        }
        if (sprint != null) {
            sprint.setState(newState);
            sprint.save();
        }
        return new ReleasesController().releases();
    }

    @With(ScrumMaster.class)
    public Result getAvailableFeatures(){
        ObjectNode result = Json.newObject();
        ObjectNode arrayFeatures = Json.newObject();
        List<BacklogEntries> features = Ebean.find(BacklogEntries.class).where().eq("projects_id",null).findList();
        for(BacklogEntries feature : features){
            arrayFeatures.put(feature.getName(), feature.getId());
        }
        result.put("features", arrayFeatures);
        return ok(result);
    }

    @With(ScrumMaster.class)
    public Result addFeatureToSprint(Integer sprintId, String stringFeaturesId){
        String[] featuresId = stringFeaturesId.split(";");
        Sprint sprint = Sprint.find.byId(sprintId);
        if(!sprint.isEditable()){
            return badRequest("You can't edit this sprint !");
        }
        for (String aFeaturesId : featuresId) {
            BacklogEntries feature = BacklogEntries.find.byId(Integer.parseInt(aFeaturesId));
            feature.setSprint(sprint);
            feature.update();
            sprint.addBacklogEntrie(feature);
            sprint.update();
            History.assignFeature(feature, sprint);
        }
        return sprint(sprintId);
    }

    @With(ScrumMaster.class)
    public Result infosSprintEdit(Integer sprintId){
        ObjectNode result = Json.newObject();
        Sprint sprint = Sprint.find.byId(sprintId);
        result.put("name",sprint.getName());
        result.put("id",sprint.getId());
        result.put("description",sprint.getDescription());
        result.put("beginDate",format.format(sprint.getStartDate()));
        result.put("endDate",format.format(sprint.getEndDate()));
        return ok(result);
    }

    @With(ScrumMaster.class)
    public Result editSprint(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int sprintId = Integer.parseInt(requestData.get("sprintId"));
        String name = requestData.get("sprintName");
        String description = requestData.get("sprintDescription");
        String endDate = requestData.get("sprintEnd");

        ObjectNode result = Json.newObject();
        Sprint sprint = Sprint.find.byId(sprintId);
        if(!sprint.isEditable()){
            return badRequest("You can't edit this sprint !");
        }
        sprint.setName(name);
        sprint.setDescription(description);

        try {
            String beginDate = requestData.get("sprintBegin");
            if (!sprint.isRunning()) {
                sprint.setStartDate(format.parse(beginDate));
            } else {
                if (Dates.getStartOfDay(format.parse(beginDate)).compareTo(sprint.getStartDate()) !=0 ) {
                    return badRequest("You can't change the start date of a running sprint ! ");
                }
            }
        } catch (Exception e) {
            return badRequest("Wrong start date");
        }
        try{
            sprint.setEndDate(format.parse(endDate));
        }catch (Exception e) {
            return badRequest("Wrong end date !");
        }

        sprint.update();
        History.editSprint(sprint);
        result.put("id", sprintId);
        return sprint(sprint.getId());
    }

	@With(ScrumMaster.class)
	public Result getDates(Integer releaseId){
		ObjectNode result = getDatesObjectNode(releaseId);
		return ok(result);
	}

    @With(ScrumMaster.class)
    public Result getDatesWithoutSprint(Integer sprintId){
        Sprint sprint = Sprint.find.byId(sprintId);
        Releases release = sprint.getReleases();
        ObjectNode result = getBaseDatesObjectNode(release);
        result.put("sprintsDates", getDatesWithoutSprintArrayNode(release, sprint));
        return ok(result);
    }

    private ObjectNode getDatesObjectNode(Integer releaseId){
        Releases target = Releases.find.byId(releaseId);
        ObjectNode object = getBaseDatesObjectNode(target);
        object.put("sprintsDates", getSprintsDatesArrayNode(target));
        return object;
    }

    private ObjectNode getBaseDatesObjectNode(Releases target) {
        ObjectNode object = Json.newObject();
        String lastReleaseDate = "";
        List<Releases> sortedReleases = Ebean.find(Releases.class).where().and(
                Expr.eq("active", true), Expr.eq("projects_id", SessionController.getProjectId()))
                .orderBy("release_date").findList();
        for(Releases release : sortedReleases){
            if(release.getId() == target.getId()){
                object.put("begin", lastReleaseDate);
            }
            lastReleaseDate = release.getReleaseDate().toString();
        }
        object.put("end", target.getReleaseDate().toString());
        return object;
    }

    private ArrayNode getSprintsDatesArrayNode(Releases release){
        ArrayNode sprintsDates = Json.newArray();
        List<Sprint> sprints = release.getSprint();
        for (Sprint current : sprints){
            if(current.getState() != Sprint.State.ABORTED) {
                sprintsDates.addAll(listSprintDates(current));
            }
        }
        return sprintsDates;
    }

    private ArrayNode getDatesWithoutSprintArrayNode(Releases release, Sprint excluded){
        ArrayNode sprintsDates = Json.newArray();
        List<Sprint> sprints = release.getSprint();
        for (Sprint current : sprints){
            if(current.getState() != Sprint.State.ABORTED && current.getId() != excluded.getId()) {
                sprintsDates.addAll(listSprintDates(current));
            }
        }
        return sprintsDates;
    }

    private ArrayNode listSprintDates(Sprint sprint) {
        ArrayNode dates = Json.newArray();
        Date begin = sprint.getStartDate();
        Date end = sprint.getEndDate();
        LocalDate beginL = Dates.toLocalDate(begin);
        LocalDate endL = Dates.toLocalDate(end);
        while (beginL.isBefore(endL) || beginL.isEqual(endL)) {
            dates.add(beginL.toString());
            beginL = beginL.plusDays(1);
        }
        return dates;
    }
}

