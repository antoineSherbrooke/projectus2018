package controllers;

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

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class TasksControllerTest extends WithApplication{

    private Http.Session session;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();


    @Before
    public void before(){
        start(fakeApplication(inMemoryDatabase()));
        InitController initController = new InitController();
        initController.initTest();
        ConstantsTest.createUser("toto", Ebean.find(Projects.class).where().eq("id",1).findUnique(), Members.EnumMem.SCRUM_MASTER);
        session = ConstantsTest.createSession("toto","foobar");
        Integer id = initController.tasksPerFeature * initController.featuresPerProject - 2;
        map.put("taskId", id.toString());
        map.put("taskName","MyTaskName");
        map.put("taskDescription","MyTaskDescription");
        map.put("taskEstimate","120");
        map.put("sprintId","1");
        map.put("featureId","1");
        map.put("taskTime","120");

    }

    @Test
    public void index(){
        int id = 1;
        request = ConstantsTest.getRequest("GET",session,routes.TasksController.index(id).url());
        Result result = route(request);
        assertEquals("This is not the page index","/projectus/task/"+id, request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void infosTaskEdit(){
        int id = 1;
        request = ConstantsTest.getRequest("GET",session,routes.TasksController.infosTaskEdit(id).url());
        Result result = route(request);
        assertEquals("This is not the page infosTaskEdit","/projectus/task/modaledit/"+id, request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    // ERROR 400 Bad Request
    @Test
    public void addTask(){
        request = ConstantsTest.getRequest("GET",session,routes.TasksController.addTask().url(),map);
        Result result = route(request);
        assertEquals("This is not the page add task","/projectus/task/add", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void editTask(){
        request = ConstantsTest.getRequest("POST",session,routes.TasksController.editTask().url(),map);
        Result result = route(request);
        assertEquals("This is not the page edit task","/projectus/task/edit", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

}
