package projetPropre;

import static org.junit.Assert.*;

import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.Helpers;
import play.test.WithApplication;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import org.junit.*;

import controllers.*;
import models.*;
import play.twirl.api.Content;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import java.io.File;
import java.util.*;

import models.*;

public class TeamTest extends WithApplication
{
	RequestBuilder request ;
	Application app;

    @Before
    public void before()
    {
    	app = fakeApplication();
    	request = new Http.RequestBuilder();
	    start(app);
    }
    
    @Test
   	public void renderTeam()
    {
    	request = new Http.RequestBuilder().method("GET").uri(routes.TeamController.team().url());

        Result result = route(app,request);
        assertEquals("This is not the page","/Team", request.uri());
        assertEquals(OK, result.status());
    }  
    
    @Test
   	public void renderPastTeam()
    {
    	request = new Http.RequestBuilder().method("GET").uri(routes.TeamController.pastTeam().url());

        Result result = route(app,request);
        assertEquals("This is not the page","/PastTeam", request.uri());
        assertEquals(OK, result.status());
    }
    
    @Test
    public void testToGoEditTeamNotOwner()
    {
    	
    	request = new Http.RequestBuilder().method("GET").uri(routes.TeamController.editTeam().url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Team", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	
    }

	@After
	public void teardown()
	{
		stop(app);
	}  
}
