package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.DynamicForm;
import play.data.FormFactory;
import java.util.List;
import javax.inject.Inject;






public class ProjectsController extends Controller {
	@Inject
	private FormFactory formFactory;

	public Result projects() {
		List<Project> listProjects = Project.find.all();
		
		Result page = null;
		if(session("project") == null) {
			session("project","P0");
		}
		if (session("user") != null) {
			switch (session("user")) {
			case "admin":
				page = ok(views.html.templates.TemplateAdmin.render("Projects", views.html.projects.Projects.render(listProjects)));
				break;

			case "owner":
				page = ok(views.html.templates.TemplateOwner.render("Projects", views.html.projects.ProjectsManagement.render(listProjects)));
				break;

			case "user":
				page = ok(views.html.templates.TemplateUser.render("Projects", views.html.projects.Projects.render(listProjects)));
				break;

			default:
				page = ok(views.html.templates.Template.render("Projects", views.html.projects.Projects.render(listProjects)));
				break;

			}
		} else {
			page = ok(views.html.templates.Template.render("Projects", views.html.projects.Projects.render(listProjects)));
		}
		return page;

	}

	public Result renderAddProject() {
		
		Result page = null;

		if (session("user") != null && session("user").equals("owner")) {
				page = ok(views.html.templates.TemplateOwner.render("AddProject",views.html.projects.NewProject.render()));
		} else {
			page = redirect(routes.ProjectsController.projects());
			flash(ConstanteMessagesFlash.ERROR, ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN);
		}
		return page;
	}

	public Result addProjects() {
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		String projectname = boundForm.get("name");
		Project newProject;
		ProjectUserParticipation ownerInProject;
		Result page;
		
		Owner owner=Owner.getOwnerByCIP(session("name"));
	
		if (boundForm.hasErrors()) {

			flash(ConstanteMessagesFlash.ERROR, ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			page = redirect(routes.HomeController.home());

		} else {
			if (Project.find.byId(projectname) == null) {
				
				newProject = new Project(projectname, owner);
				newProject.save();
				ownerInProject=new ProjectUserParticipation(session("name"),projectname);
				ownerInProject.inActivity();
				owner.addProject(newProject);
				owner.save();
				flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_OK));
				page= redirect(routes.ProjectsController.projects());
			}else {
				page = redirect(routes.ProjectsController.renderAddProject());
			}
			
		}
		return page;
	
	}
}