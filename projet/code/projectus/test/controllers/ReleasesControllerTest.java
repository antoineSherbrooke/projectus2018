package controllers;

import static org.junit.Assert.assertEquals;

import com.avaje.ebean.Ebean;
import models.Members;
import models.Projects;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.*;

public class ReleasesControllerTest extends WithApplication{

    private Http.Session session;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();
    private int id;

    @Before
    public void before(){
        start(fakeApplication(inMemoryDatabase()));
        new InitController().initTest();
        ConstantsTest.createUser("toto", Ebean.find(Projects.class).where().eq("id",1).findUnique(), Members.EnumMem.PRODUCT_OWNER);
        session = ConstantsTest.createSession("toto","foobar");
        id = 1;
        map.put("releaseName","MyReleaseName");
        map.put("releaseDescription","MyReleaseDescription");
        map.put("releaseEndDate","12-04-1996");
        map.put("releaseId","1");
    }

    @Test
    public void index() {
        request = ConstantsTest.getRequest("GET",session,routes.ReleasesController.index().url());
        Result result = route(request);
        assertEquals("This is not the page releases", "/projectus/releases" , request.uri());
        assertEquals("The page should be work", OK , result.status());
        //assertTrue("This isn't the good page", contentAsString(result).contains("No release."));
    }

    @Test
    public void infoReleaseEdit(){
        request = ConstantsTest.getRequest("POST",session,routes.ReleasesController.infoReleaseEdit(id).url(),map);
        Result result = route(request);
        assertEquals("This is not the page infoReleaseEdit", "/projectus/releases/info_release_edit/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void editRelease(){
        request = ConstantsTest.getRequest("POST",session,routes.ReleasesController.editRelease().url(),map);
        Result result = route(request);
        assertEquals("This is not the page editRelease", "/projectus/release/edit_release" , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void addRelease(){
        request = ConstantsTest.getRequest("POST",session,routes.ReleasesController.addRelease().url(),map);
        Result result = route(request);
        assertEquals("This is not the page addRelease", "/projectus/release/add" , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void infosRelease(){
        request = ConstantsTest.getRequest("GET",session,routes.ReleasesController.infosRelease(id).url());
        Result result = route(request);
        assertEquals("This is not the page infosRelease", "/projectus/release/infos/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void dialDisableRelease(){
        request = ConstantsTest.getRequest("GET",session,routes.ReleasesController.dialDisableRelease(id).url());
        Result result = route(request);
        assertEquals("This is not the page dialDisableRelease", "/projectus/dial_disable_release/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void disableRelease(){
        request = ConstantsTest.getRequest("GET",session,routes.ReleasesController.disableRelease(id).url());
        Result result = route(request);
        assertEquals("This is not the page disableRelease", "/projectus/disable_release/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }

    @Test
    public void activateRelease(){
        request = ConstantsTest.getRequest("GET",session,routes.ReleasesController.activateRelease(id).url());
        Result result = route(request);
        assertEquals("This is not the page activateRelease", "/projectus/activate_release/"+id , request.uri());
        assertEquals("The page should be work", OK , result.status());
    }
}
