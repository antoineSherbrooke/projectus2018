package controllers;

import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ProjectControllerTest extends WithApplication {

    private Http.Session session;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();


    @Before
    public void before() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        new InitController().initTest();
        session = ConstantsTest.createSession("2","foobar");

        map.put("team_name","MyTeamName");
        map.put("email","MyEmail");
        map.put("description","MyDescription");
    }

    @Test
    public void infoProject(){
        request = ConstantsTest.getRequest("POST",session,routes.ProjectController.infoProject().url());
        Result result = route(request);
        assertEquals("This is not the page project info","/projectus/project_info/modal_add_project", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void editProject(){
        request = ConstantsTest.getRequest("POST",session,routes.ProjectController.editProject().url(),map);
        Result result = route(request);
        assertEquals("This is not the page edit project","/projectus/project_info/edit", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }
}
