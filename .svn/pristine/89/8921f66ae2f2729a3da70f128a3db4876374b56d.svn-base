package controllers;

import com.avaje.ebean.Ebean;
import models.Members;
import models.Projects;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class SprintsControllerTest extends WithApplication {

    private Http.Session session;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();
    private int id;

    @Before
    public void before(){
        start(ConstantsTest.fakeApp());
        new InitController().initTest();
        ConstantsTest.createUser("toto", Ebean.find(Projects.class).where().eq("id",1).findUnique(), Members.EnumMem.SCRUM_MASTER);
        session = ConstantsTest.createSession("toto","foobar");
        id = 1;
        map.put("sprintRelease","1");
        map.put("sprintName","MySprintName");
        map.put("sprintBeginDate","12-04-1996");
        map.put("sprintEndDate","12-04-2016");
        map.put("sprintBegin","12-04-1996");
        map.put("sprintEnd","12-04-2016");
        map.put("sprintDescription","MySprintDescription");
        map.put("sprintId","1");
    }
    @Test
    public void index() {
        request = ConstantsTest.getRequest("GET",session,routes.SprintsController.index(id).url());
        Result result = route(request);
        assertEquals("This is not the page sprint", "/projectus/sprint/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }
    @Test
    public void addSprint() {
        request = ConstantsTest.getRequest("POST",session,routes.SprintsController.addSprint().url(),map);
        Result result = route(request);
        assertEquals("This is not the page addSprint", "/projectus/sprint/add" , request.uri());
        //ERROR 400
        assertEquals("The page should be work", OK , result.status());
    }
    @Test
    public void abortSprint() {
        request = ConstantsTest.getRequest("POST",session,routes.SprintsController.abortSprint(id).url(),map);
        Result result = route(request);
        assertEquals("This is not the page abortSprint", "/projectus/sprint/abort/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }
    @Test
    public void startSprint() {
        request = ConstantsTest.getRequest("POST",session,routes.SprintsController.startSprint(id).url(),map);
        Result result = route(request);
        assertEquals("This is not the page startSprint", "/projectus/sprint/start/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void getAvailableFeatures() {
        request = ConstantsTest.getRequest("GET",session,routes.SprintsController.getAvailableFeatures().url());
        Result result = route(request);
        assertEquals("This is not the page getAvailableFeatures", "/projectus/get/available-features" , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void getDates() {
        request = ConstantsTest.getRequest("GET",session,routes.SprintsController.getDates(id).url());
        Result result = route(request);
        assertEquals("This is not the page getDates", "/projectus/release/dates/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void addFeatureToSprint() {
        request = ConstantsTest.getRequest("POST",session,routes.SprintsController.addFeatureToSprint(id,"1;2;3").url());
        Result result = route(request);
        assertEquals("This is not the page addFeatureToSprint", "/projectus/sprint/add/feature/"+id+"/1;2;3" , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void infosSprintEdit() {
        request = ConstantsTest.getRequest("GET",session,routes.SprintsController.infosSprintEdit(id).url());
        Result result = route(request);
        assertEquals("This is not the page infosSprintEdit", "/projectus/sprint/modaledit/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void editSprint() {
        request = ConstantsTest.getRequest("POST",session,routes.SprintsController.editSprint().url(),map);
        Result result = route(request);
        assertEquals("This is not the page editSprint", "/projectus/sprint/edit" , request.uri());
        //ERROR 400
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void getDatesWithoutSprint() {
        request = ConstantsTest.getRequest("GET",session,routes.SprintsController.getDatesWithoutSprint(id).url());
        Result result = route(request);
        assertEquals("This is not the page getDatesWithoutSprint", "/projectus/sprint/dates/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }
}
