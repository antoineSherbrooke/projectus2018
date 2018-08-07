package controllers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import Security.ProductOwner;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import models.BacklogEntries.EnumPriority;
import models.BacklogEntries.EnumFirstEstimate;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.product_backlog;
import views.html.infos_feature;
import views.html.add_feature_tosprint;
import views.html.product_backlog_solo;

import javax.inject.Inject;

@With(Secure.class)
public class ProductBacklogController extends Controller {

	@Inject
	FormFactory formFactory;

	public Result index(){
        return ok(product_backlog.render(getBacklogEntriesActive(), getBacklogEntriesDisactive()));
	}

	public Result productBacklog(){
		return ok(product_backlog_solo.render(getBacklogEntriesActive(),  getBacklogEntriesDisactive()));
	}

	public List<BacklogEntries> getBacklogEntriesActive(){
		return Ebean.find(BacklogEntries.class).where().and(Expr.eq("projects_id", SessionController.getProjectId()), Expr.eq("activate", true)).findList();
	}

	public List<BacklogEntries> getBacklogEntriesDisactive(){
		return Ebean.find(BacklogEntries.class).where().and(Expr.eq("projects_id", SessionController.getProjectId()), Expr.eq("activate", false)).findList();
	}

    @With(ProductOwner.class)
	public Result addFeature(){
		DynamicForm requestData = formFactory.form().bindFromRequest();
		String name = requestData.get("featureName");
		String priority = requestData.get("featurePriority");
		String firstEstimate = requestData.get("featureDuration");
		String description = requestData.get("featureDesc");

		EnumPriority enumPriority = EnumPriority.fromString(priority);
        EnumFirstEstimate enumFirstEstimate = EnumFirstEstimate.valueOf(firstEstimate);
		Projects runningProject = SessionController.getProjectRunning();
		if(name.isEmpty() || description.isEmpty()){
			return badRequest("Fields are required");
		}
		int numberOfFeature = runningProject.getBacklogEntries().size()+1;
		BacklogEntries backlogEntries = new BacklogEntries(name,description,enumPriority,enumFirstEstimate,runningProject);
		backlogEntries.setNumber(numberOfFeature);
		backlogEntries.save();
        History.newFeature(backlogEntries);
		return productBacklog();

	}

	public Result getInfos(Integer featureId, Boolean editable){
		BacklogEntries feature = Ebean.find(BacklogEntries.class).where().eq("id",featureId).orderBy("id").findUnique();
		return ok(infos_feature.render(feature, editable));
	}

    public Result getDatas(Integer featureId){
        ObjectNode result = Json.newObject();
        ArrayNode datasDev = getDatasDev(featureId);
		ObjectNode datasDay = getDatasDay(featureId);

        result.put("datasDev", datasDev);
		result.put("datasDay", datasDay);
        return ok(result);
    }

	public ArrayNode getDatasDev(Integer featureId){
		ArrayNode datas = Json.newArray();
		Projects projects= SessionController.getProjectRunning();
		Groups group = projects.getGroups();
		List<Members> members = group.getMembers();
		BacklogEntries feature = Ebean.find(BacklogEntries.class).where().eq("id",featureId).orderBy("id").findUnique();
		List<SprintTasks> featuresTasks = feature.getSprintTasks();
		for(Members member : members){
			if (member.getMemberType() == Members.EnumMem.DEVELOPER) {
				Integer time = 0;
				ObjectNode mem = Json.newObject();
				List<MembersTasksDoing> memberTasksDoings = member.getMembersTaskDoings();
				List<MembersTasksReview> memberTasksReview = member.getMembersTasksReviews();
				String name = member.getAssociations().getUsers().getFirstName();
				for (MembersTasksDoing memTaskDoing : memberTasksDoings) {
					if (featuresTasks.contains(memTaskDoing.getSprintTasks())) {
						time = time + memTaskDoing.getTimeSpent();
					}
				}
				for (MembersTasksReview memTaskReview : memberTasksReview) {
					if (featuresTasks.contains(memTaskReview.getSprintTasks())) {
						time = time + memTaskReview.getTimeSpent();
					}
				}
				mem.put(name, time);
				datas.add(mem);
			}
		}
		return datas;
	}

	public ObjectNode getDatasDay(Integer featureId){
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
        ObjectNode obj = Json.newObject();
		List<SprintTasks> featuresTasks = Ebean.find(SprintTasks.class).where().eq("backlog_entries_id",featureId).orderBy("start_date").findList();;
		for(SprintTasks task : featuresTasks){
            List<MembersTasksReview> membersTasksR = task.getMembersTasksReviews();
            List<MembersTasksDoing> membersTasksD = task.getMembersTaskDoings();
            for(MembersTasksReview membersTaskR : membersTasksR){
				String date = formater.format(membersTaskR.getDay()).toString();
                if(!obj.has(date)){
                    Integer time = membersTaskR.getTimeSpent();
                    obj.put(date, time);
                }else {
                    Integer time = obj.findValue(date).asInt() + membersTaskR.getTimeSpent();
                    obj.remove(date);
                    obj.put(date, time);
                }
            }

            for(MembersTasksDoing membersTaskD : membersTasksD){
				String date2 = formater.format(membersTaskD.getDay()).toString();
                if(!obj.has(date2)){
                    Integer time = membersTaskD.getTimeSpent();
                    obj.put(date2, time);
                }else {
                    Integer time = obj.findValue(date2).asInt() + membersTaskD.getTimeSpent();
                    obj.remove(date2);
                    obj.put(date2, time);
                }
            }
		}
		return obj;
	}

	@With(ProductOwner.class)
	public Result editFeature(){
		DynamicForm requestData = formFactory.form().bindFromRequest();
		Integer id = Integer.parseInt(requestData.get("featureId"));
		String name = requestData.get("featureName");
		String priority = requestData.get("featurePriority");
		String firstEstimate = requestData.get("featureDuration");
		String description = requestData.get("featureDesc");

		EnumPriority enumPriority = EnumPriority.fromString(priority);
		EnumFirstEstimate enumFirstEstimate = EnumFirstEstimate.valueOf(firstEstimate);

		if(name.isEmpty() || description.isEmpty()){
			return badRequest("Fields are required");
		}
		BacklogEntries feature = Ebean.find(BacklogEntries.class).where().eq("id",id).findUnique();

		feature.setName(name);
		feature.setPriority(enumPriority);
		feature.setFirstEstimate(enumFirstEstimate);
		feature.setDescription(description);

		feature.update();
        History.editFeature(feature);
		return productBacklog();
	}

	public Result getFeatures(Integer sprintId){
		Sprint sprint = Sprint.find.byId(sprintId);
        if(sprint.getReleases().getProjects().getId() != SessionController.getProjectRunning().getId()){
            return badRequest("This sprint don't belong to your project !");
        }
        List<BacklogEntries> featuresTmp = SessionController.getProjectRunning().getBacklogEntries();
        List<BacklogEntries> features = featuresTmp.stream().filter(b -> !b.getSprints().contains(sprint) && b.getActivate()).collect(Collectors.toList());
		return ok(add_feature_tosprint.render(features, sprint));
	}

    @With(ProductOwner.class)
    public Result disableFeature(Integer featureId){
        BacklogEntries feature = BacklogEntries.find.byId(featureId);
        if(!feature.getSprintTasks().isEmpty()){
            return badRequest("This feature contain tasks !");
        }
        feature.setActivate(false);
        feature.update();
        return productBacklog();
    }

    @With(ProductOwner.class)
    public Result activateFeature(Integer featureId){
        BacklogEntries feature = BacklogEntries.find.byId(featureId);
        feature.setActivate(true);
        feature.update();
        return productBacklog();
    }
}

