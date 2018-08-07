package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Groups;
import models.Projects;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.project_infos;

import javax.inject.Inject;

public class ProjectController extends Controller{

    @Inject
    FormFactory formFactory;

    public Result index(){
        return ok(project_infos.render(SessionController.getProjectRunning(),SessionController.getProjectRunning().getGroups()));
    }


    public Result infoProject(){
        ObjectNode result = Json.newObject();
        Projects project = SessionController.getProjectRunning();
        Groups group = SessionController.getProjectRunning().getGroups();
        result.put("name",project.getName());
        result.put("description",project.getDescription());
        result.put("email",group.getEmail());
        result.put("team_name",group.getName());
        return ok(result);
    }

    public Result editProject(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String teamName = requestData.get("team_name");
        String email = requestData.get("email");
        String description = requestData.get("description");

        Projects project = SessionController.getProjectRunning();
        Groups group = SessionController.getProjectRunning().getGroups();
        project.setDescription(description);
        group.setName(teamName);
        if(!email.equals("null")) {
            group.setEmail(email);
        }else{
            group.setEmail("");
        }
        project.update();
        group.update();
        ObjectNode result = Json.newObject();
        result.put("state","success");
        return ok();
    }

}
