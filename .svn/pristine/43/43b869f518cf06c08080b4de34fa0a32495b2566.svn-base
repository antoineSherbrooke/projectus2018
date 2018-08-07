package controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class DashboardControllerTest extends WithApplication{

    private Http.Session session;
    private Http.RequestBuilder request;

    @Before
    public void before(){
	    start(fakeApplication(inMemoryDatabase()));
        new InitController().initTest();
        ConstantsTest.createUser("test1111");
        session = ConstantsTest.createSession("test1111", "foobar");
    }

    @After
    public void cleanup(){}
	  
    @Test
    public void dashboard() {
        request = ConstantsTest.getRequest("GET",session,routes.DashboardController.dashboard().url());
        Result result = route(request);
        assertEquals("This is not the page dashboard","/projectus/dashboard", request.uri());
        assertEquals("The page should be work", OK, result.status());
        assertTrue("Le sprint de devrais pas être activée", contentAsString(result).contains("No running sprint."));
    }

    @Test
    public void infoTask(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("id_task", "139");
        request = ConstantsTest.getRequest("POST",session,routes.DashboardController.infoTask().url(),map);
        Result result = route(request);
        assertEquals("This is not the page info task","/projectus/info/task", request.uri());
        assertEquals("The page should be work", OK, result.status());
        assertTrue("The content of the page is false", contentAsString(result).contains("Time for task"));
    }

    @Test
    public void addComment() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id_task", "139");
        map.put("text", "Hi");
        request = ConstantsTest.getRequest("POST",session,routes.DashboardController.addComment().url(),map);
        Result result = route(request);
        assertEquals("This is not the page for add comment","/projectus/add_com", request.uri());
        assertEquals("The page should be work", OK, result.status());
        assertTrue("The content of the page is false", contentAsString(result).contains("member_name"));
    }

}
