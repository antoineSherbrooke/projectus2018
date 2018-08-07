package controllers;

import Security.SuperAdministrator;
import com.avaje.ebean.Ebean;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.superadministrator;
import views.html.project;
import views.html.supervisor;
import views.html.supervisor_dialog;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.List;

@With(SuperAdministrator.class)
public class AdministratorController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result superadministrators() {
        List<Associations> associations = Associations.find.where().eq("association_type", Associations.EnumAsso.ADMINISTRATOR).findList();
        return ok(superadministrator.render(associations));
    }

    public Result supervisors() {
        List<Projects> projects = Projects.find.all();
        return ok(supervisor.render(getSupervisors(),projects));
    }

    public Result projects() {
        List<Projects> projects = Projects.find.all();
        return ok(project.render(projects, getSupervisors()));
    }

    public Result addSuperAdministrator() {
        try {

            DynamicForm requestData = formFactory.form().bindFromRequest();
            String cip = requestData.get("cip");
            String firstName = requestData.get("firstName");
            String lastName = requestData.get("lastName");
            Users user = new Users(cip, firstName, lastName);
            user.save();
            try {
                Associations association = new Associations(user, Associations.EnumAsso.ADMINISTRATOR);
                association.save();
            } catch (PersistenceException e) {
                flash("error", "A error was occuried");
            }
        } catch (PersistenceException e) {
            flash("error", "The cip already exist");
        }

        return redirect(routes.AdministratorController.superadministrators().url());
    }

    public Result addSupervisor() {
        try {
            DynamicForm requestData = formFactory.form().bindFromRequest();
            String cip = requestData.get("cip");
            String firstName = requestData.get("firstName");
            String lastName = requestData.get("lastName");
            Projects targetProject = Projects.find.byId(Integer.parseInt(requestData.get("targetProjectId")));
            try {
                //Set of the user to be a supervisor
                Users user = Users.newUser(cip, firstName, lastName, targetProject);
                user.save();

                //Creation of the association needed to be a supervisor
                Associations association = new Associations(user, Associations.EnumAsso.SUPERVISOR);

                //Creation of the object supervisor
                Supervisors supervisors = new Supervisors(association);
                supervisors.save();

                //Set of the association between the object supervisor and the user
                association.setSupervisors(supervisors);
                association.save();

                //Association between the project and the supervisor
                targetProject.getSupervisors().add(supervisors);
                supervisors.getProjects().add(targetProject);
                targetProject.update();
                supervisors.update();

            } catch (Exception e) {
                badRequest(e.getMessage());
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            flash("error", "The cip already exist");
        }
        return redirect(routes.AdministratorController.supervisors().url());
    }

    private List<Supervisors> getSupervisors() {
        return Supervisors.find.all();
    }


    public Result addProject() {

        Ebean.beginTransaction();
        try {
            DynamicForm requestData = formFactory.form().bindFromRequest();
            String name = requestData.get("name");
            String nameShort = name.substring(0, 4).toLowerCase();
            String description = requestData.get("description");
            String team = requestData.get("team");
            String email = requestData.get("email");

            Projects projects = new Projects(description, name, true);
            projects.save();

            Groups group = new Groups(team, email, projects);
            group.save();

            projects.setGroups(group);

            int id_supervisor = Integer.parseInt(requestData.get("supervisor"));
            if (id_supervisor > 0) {
                Supervisors supervisor = Supervisors.find.byId(id_supervisor);
                supervisor.getProjects().add(projects);
                supervisor.update();
            }

            Users userAdmin = new Users(nameShort + "admi1001", "Administrator", "AM");
            Users userScrum = new Users(nameShort + "mass1001", "Scrum Master", "SM");
            Users userProd = new Users(nameShort + "ownp1001", "Product Owner", "PO");
            userAdmin.save();
            userScrum.save();
            userProd.save();

            Associations associationAdmin = new Associations(userAdmin, Associations.EnumAsso.MEMBER);
            Associations associationScrum = new Associations(userScrum, Associations.EnumAsso.MEMBER);
            Associations associationProd = new Associations(userProd, Associations.EnumAsso.MEMBER);
            associationAdmin.save();
            associationScrum.save();
            associationProd.save();

            Members membersAdmin = new Members(associationAdmin, group, Members.EnumMem.ADMINISTRATOR);
            Members membersScrum = new Members(associationScrum, group, Members.EnumMem.SCRUM_MASTER);
            Members membersProd = new Members(associationProd, group, Members.EnumMem.PRODUCT_OWNER);

            associationAdmin.setMembers(membersAdmin);
            associationScrum.setMembers(membersScrum);
            associationProd.setMembers(membersProd);

            membersAdmin.save();
            membersScrum.save();
            membersProd.save();

            Ebean.commitTransaction();

        } catch (PersistenceException e) {
            flash("error", "Error when inserting a new project, 4 first letters of the project already be used !");
        }

        Ebean.endTransaction();

        return redirect(routes.AdministratorController.projects().url());
    }

    public Result addProjectToSupervisor(Integer id_supervisor) {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Integer id_project = Integer.parseInt(requestData.get("project"));
        Projects project = Projects.find.byId(id_project);
        Supervisors supervisor = Supervisors.find.byId(id_supervisor);
        project.getSupervisors().add(supervisor);
        supervisor.getProjects().add(project);
        project.update();
        supervisor.update();
        return redirect(routes.AdministratorController.supervisors().url());
    }

    public Result getDialogProjectSupervisor() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int id_supervisor = Integer.parseInt(requestData.get("id_supervisor"));
        List<Projects> project = Projects.find.all();
        Supervisors supervisor = Supervisors.find.byId(id_supervisor);
        project.removeAll(supervisor.getProjects());
        return ok(supervisor_dialog.render(project, id_supervisor));
    }

    public Result removeProjectOfSupervisor(Integer id_project, Integer id_supervisor) {
        Projects project = Projects.find.byId(id_project);
        Supervisors supervisor = Supervisors.find.byId(id_supervisor);
        project.getSupervisors().remove(supervisor);
        supervisor.getProjects().remove(project);
        project.update();
        supervisor.update();
        return redirect(routes.AdministratorController.supervisors().url());
    }
}
