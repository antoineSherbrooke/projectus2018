package controllers;

import Security.ScrumMaster;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.dashboard_task;

import javax.inject.Inject;


@With(Secure.class)
public class TasksController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result index(Integer taskId){
        SprintTasks task = Ebean.find(SprintTasks.class).where().eq("id",taskId).findUnique();
		return ok(dashboard_task.render(task, null, null, false));
	}

    @With(ScrumMaster.class)
    public Result infosTaskEdit(Integer taskId){
        ObjectNode result = Json.newObject();
        SprintTasks task = Ebean.find(SprintTasks.class).where().eq("id",taskId).findUnique();
        result.put("name",task.getName());
        result.put("id",task.getId());
        result.put("description",task.getDescription());
        result.put("estimate",task.getEstimate());
        return ok(result);
    }

    @With(ScrumMaster.class)
    public Result addTask() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int sprintId = Integer.parseInt(requestData.get("sprintId"));
        int featureId = Integer.parseInt(requestData.get("featureId"));
        String name = requestData.get("taskName");
        String description = requestData.get("taskDescription");
        int estimate = Integer.parseInt(requestData.get("taskTime"));

        Sprint sprint = Sprint.find.byId(sprintId);
        if(sprint.isRunning()){
            return badRequest("You can't add a task on a running sprint !");
        }
        BacklogEntries feature = Ebean.find(BacklogEntries.class).where().eq("id",featureId).findUnique();
        SprintTasks task = new SprintTasks(name, SprintTasks.EnumState.TODO, feature, estimate, sprint, description);
        task.save();
        History.newTask(task);
        return new SprintsController().sprint(sprintId);
    }

    @With(ScrumMaster.class)
    public Result editTask(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int taskId = Integer.parseInt(requestData.get("taskId"));
        String name = requestData.get("taskName");
        String description = requestData.get("taskDescription");
        int estimate = Integer.parseInt(requestData.get("taskEstimate"));

        ObjectNode result = Json.newObject();
        SprintTasks sprintTask = SprintTasks.find.byId(taskId);

        if(!sprintTask.getSprint().isEditable()){
            return badRequest("You can't edit a task this sprint");
        }
        sprintTask.setName(name);
        sprintTask.setDescription(description);
        sprintTask.setEstimate(estimate);
        sprintTask.update();
        History.editTask(sprintTask);
        result.put("id", taskId);
        return new SprintsController().sprint(sprintTask.getSprint().getId());
    }


}

