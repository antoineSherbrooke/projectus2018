package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.DynamicForm;
import play.data.FormFactory;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class TeamController extends Controller {
	@Inject
	private FormFactory formFactory;

	public Result team() {

		if (session("project") == null) {
			session("project", "P0");
		}
		List<Users> listActive = ProjectUserParticipation.getProjectsUserActive(Project.find.byId(session("project")));
		Result page = null;
		if (session("user") != null) {
			switch (session("user")) {
			case "admin":
				page = ok(views.html.templates.TemplateAdmin.render("Team",views.html.teams.Team.render(listActive, Status.find.all())));
				break;

			case "owner":
				if (Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
					page = ok(views.html.templates.TemplateOwner.render("Team",views.html.teams.TeamManagement.render(listActive, Status.find.all())));
				} else {
					page = ok(views.html.templates.TemplateOwner.render("Team",views.html.teams.Team.render(listActive, Status.find.all())));
				}
				break;

			case "user":
				page = ok(views.html.templates.TemplateUser.render("Team",views.html.teams.Team.render(listActive, Status.find.all())));
				break;

			default:
				page = ok(views.html.templates.Template.render("Team",views.html.teams.Team.render(listActive, Status.find.all())));
				break;
			}
		} else {
			page = ok(views.html.templates.Template.render("Team",views.html.teams.Team.render(listActive, Status.find.all())));
		}
		return page;
	}

	public Result pastTeam() {

		if (session("project") == null) {
			session("project", "P0");
		}

		List<Users> listInactive = ProjectUserParticipation.getProjectsUserInactive(Project.find.byId(session("project")));
		Result page = null;

		if (session("user") != null) {
			switch (session("user")) {
			case "admin":
				page = ok(views.html.templates.TemplateAdmin.render("Past Team",views.html.teams.PastTeam.render(listInactive, Status.find.all())));
				break;

			case "owner":
				if (Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
					page = ok(views.html.templates.TemplateOwner.render("Past Team",views.html.teams.PastTeamManagement.render(listInactive, Status.find.all())));
				} else {
					page = ok(views.html.templates.TemplateOwner.render("Past Team",views.html.teams.PastTeam.render(listInactive, Status.find.all())));
				}
				break;

			case "user":
				page = ok(views.html.templates.TemplateUser.render("Past Team",views.html.teams.PastTeam.render(listInactive, Status.find.all())));
				break;

			default:
				page = ok(views.html.templates.Template.render("Past Team",views.html.teams.PastTeam.render(listInactive, Status.find.all())));
				break;

			}
		} else {
			page = ok(views.html.templates.Template.render("Past Team",views.html.teams.PastTeam.render(listInactive, Status.find.all())));
		}
		return page;
	}

	public Result editTeam() {

		if (session("project") == null) {
			session("project", "P0");
		}
		
		Result page = null;
		List<Users> listUsersInProject = ProjectUserParticipation.getProjectsUser(Project.find.byId(session("project")));
		ArrayList<Users> listUsersNotInProject = this.getNotMembers(listUsersInProject);
		List<Users> listActive = ProjectUserParticipation.getProjectsUserActive(Project.find.byId(session("project")));
		List<Users> listInactive = ProjectUserParticipation.getProjectsUserInactive(Project.find.byId(session("project")));

		if (session("user") != null && session("user").equals("owner")) {

				if (Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
					page = ok(views.html.templates.TemplateOwner.render("Edit Team",views.html.teams.EditTeam.render(listActive,listInactive,listUsersNotInProject)));
				} else {
					flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_PROJECT_RIGHT));
					page = redirect(routes.TeamController.team());
				}
				
		} else {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
			page = redirect(routes.TeamController.team());
		}
		return page;
	}
	
	public Result postAddUserTeam() {
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		if(boundForm.get("send").equals("cancel")) {
			flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITHOME_CANCEL));
		}else {  
			String cipUser = boundForm.get("cip");
			ProjectUserParticipation addUserTeam = new ProjectUserParticipation(cipUser, session("project"));
			addUserTeam.save();
			flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITTEAM_OK));
		}
		return redirect(routes.TeamController.editTeam());
	}
	
	private ArrayList<Users> getNotMembers(List<Users> listUsersInProject){
		List<Users> listUsers = Users.find.all();
		ArrayList<Users> listUsersNotInProject = new ArrayList<Users>();
		for (int i = 0; i < listUsers.size(); i++) {
			int val = 0;
			for (int j = 0; j < listUsersInProject.size(); j++) {
				if (listUsers.get(i).equals(listUsersInProject.get(j))) {
					val = 0;
				} else {
					val++;
				}
			}
			if (val == listUsersInProject.size()) {
				listUsersNotInProject.add(listUsers.get(i));
			}
		}
		return listUsersNotInProject;
	}
	

	public Result PostDesactivateUserTeam(String cip) {

			
		Users user = Users.find.byId(cip);
		Project project = Project.find.byId(session("project"));
		
		ProjectUserParticipation deleteUserTeam = ProjectUserParticipation.getParticipationFromUserProject(user, project);
		deleteUserTeam.offActivity();
		deleteUserTeam.save();
		flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITTEAM_OK));
		
		return redirect(routes.TeamController.editTeam());
	}
	public Result PostActivateUserTeam(String cip) {

		Users user = Users.find.byId(cip);
		Project project = Project.find.byId(session("project"));
		
		ProjectUserParticipation deleteUserTeam = ProjectUserParticipation.getParticipationFromUserProject(user, project);
		deleteUserTeam.inActivity();
		deleteUserTeam.save();
		flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITTEAM_OK));
		
		return redirect(routes.TeamController.editTeam());
	}
	
	public Result PostAddUserTeam(String cip) {

		ProjectUserParticipation addUserTeam = new ProjectUserParticipation(cip, session("project"));
		addUserTeam.save();
		flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITTEAM_OK));
		
		return redirect(routes.TeamController.editTeam());
	}
	
	public Result DeleteUserTeam(String cip){
		
		Users user = Users.find.byId(cip);
		Project project = Project.find.byId(session("project"));
		ProjectUserParticipation deleteUserTeam = ProjectUserParticipation.getParticipationFromUserProject(user, project);
		deleteUserTeam.delete();
		flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITTEAM_OK));
		return redirect(routes.TeamController.editTeam());
		
	}

}