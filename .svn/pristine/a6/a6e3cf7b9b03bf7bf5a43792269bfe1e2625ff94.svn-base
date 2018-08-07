package controllers;

import models.*;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class AdministratorControllerTest {


    private Http.Session session;
    private Http.RequestBuilder request;

    @Before
    public void before() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        Users admin = new Users("superadmin", "A", "B", "foobar");
        admin.save();
        Associations associations = new Associations(admin, Associations.EnumAsso.ADMINISTRATOR);
        associations.save();
        session = ConstantsTest.createSession("superadmin", "foobar");

    }


    @Test
    public void superadministrators() throws Exception {
        request = ConstantsTest.getRequest("GET",session,routes.AdministratorController.superadministrators().url());
        Result result = route(request);
        assertEquals("This is not the page superadministrator","/projectus/superadministrators", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void supervisors() throws Exception {
        request = ConstantsTest.getRequest("GET",session,routes.AdministratorController.supervisors().url());
        Result result = route(request);
        assertEquals("This is not the page supervisor","/projectus/supervisors", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void projects() throws Exception {
        request = ConstantsTest.getRequest("GET",session,routes.AdministratorController.projects().url());
        Result result = route(request);
        assertEquals("This is not the page projects","/projectus/projects", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void addSuperAdministrator() throws Exception {
        Map<String, String> post = new HashMap<>();
        post.put("cip", "mare2423");
        post.put("firstName", "Joe");
        post.put("lastName", "Dalton");
        request = ConstantsTest.getRequest("POST",session,routes.AdministratorController.addSuperAdministrator().url());
        request.bodyForm(post);
        Result result = route(request);
        assertEquals("This is not the page superadministrator","/projectus/superadministrators", result.redirectLocation().get());
        assertEquals("The page should be work", ConstantsTest.REDIRECTION, result.status());
        assertTrue("The superadministrator have not added", Users.find.where().eq("cip", "mare2423").findUnique() != null);
    }

    @Test
    public void addSupervisor() throws Exception {
        Users supervisorUser = new Users("mare2442","Willy","Dalton");
        Map<String, String> post = new HashMap<>();
        post.put("cip", supervisorUser.getCip());
        post.put("firstName", supervisorUser.getFirstName());
        post.put("lastName", supervisorUser.getLastName());
        Projects targetProject = new Projects("Description","Name of the target project",true);
        Groups groupsOfTargetProject = new Groups(targetProject,"Group Name");
        groupsOfTargetProject.save();
        targetProject.setGroups(groupsOfTargetProject);
        targetProject.save();
        post.put("targetProjectId",Integer.toString(targetProject.getId()));

        request = ConstantsTest.getRequest("POST",session,routes.AdministratorController.addSupervisor().url());
        request.bodyForm(post);
        Result result = route(request);
        assertEquals("This is not the page superadministrator","/projectus/supervisors", result.redirectLocation().get());
        assertEquals("The page should be work", ConstantsTest.REDIRECTION, result.status());
        assertEquals("The supervisor's cip is wrong",supervisorUser.getCip() ,Users.find.where().eq("cip", supervisorUser.getCip()).findUnique().getCip());
        assertEquals("The supervisor's first name is wrong",supervisorUser.getFirstName() ,Users.find.where().eq("firstName", supervisorUser.getFirstName()).findUnique().getFirstName());
        assertEquals("The supervisor's last is wrong",supervisorUser.getLastName() ,Users.find.where().eq("lastName", supervisorUser.getLastName()).findUnique().getLastName());
    }

    @Test
    public void addProject() throws Exception {
        Map<String, String> post = new HashMap<>();
        post.put("name", "Testpro");
        post.put("description", "Rien");
        post.put("team", "TestTeam");
        post.put("supervisor", "-1");
        post.put("email", " ");
        request = ConstantsTest.getRequest("POST",session,routes.AdministratorController.addProject().url(),post);
        Result result = route(request);
        assertEquals("This is not the page superadministrator","/projectus/projects", result.redirectLocation().get());
        assertEquals("The page should be work", ConstantsTest.REDIRECTION, result.status());
        assertTrue("The supervisor have not added", Projects.find.where().eq("name", "Testpro").findUnique() != null);
    }
}