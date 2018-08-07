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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.inject.Inject;
import controllers.HomeController;
import controllers.routes;
import play.twirl.api.Content;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import models.Administrator;
import models.Owner;
import models.Project;
import models.Status;
import models.Users;
import play.data.DynamicForm;
import play.data.FormFactory;

public class HomeTest extends WithApplication{		

	private final String CIP = "PseudoAdminTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	private final String STATUS = "Test";
	private final String USERO = "owner";
	private final String PROJECT = "PTest";
	
	private Users userOTest;
	private Owner ownerTest;
	private Project pTest;
	
	RequestBuilder request ;
	Application app;
	Http.Session session;
	Project projecttest;
	Status statustest;

	
	
    @Before
    public void before(){
    	
    	
    	statustest=new Status(STATUS);
    	statustest.save();
    	userOTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
    	userOTest.save();
    	ownerTest = new Owner(userOTest);
    	ownerTest.save();
    	pTest= new Project(PROJECT,"","",ownerTest);
    	pTest.save();
    	
    	app = fakeApplication();
        request = new Http.RequestBuilder();
        start(app);
        
    }
    
    @Test
    public void renderHome(){
    	
    	request = new Http.RequestBuilder().method("GET").uri(routes.HomeController.homes(PROJECT).url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/"+PROJECT, request.uri());
        assertEquals(OK, result.status());
        
    }
    
    @Test
    public void renderHomeOwner(){
    	
    	Map<String,String> sessionOwner=new HashMap<String,String>();
    	sessionOwner.put("name", CIP);
    	sessionOwner.put("user", USERO);
    	Session session=new Session(sessionOwner);
    	session.put("name", CIP);
    	session.put("user", USERO);
    	
    	request = new Http.RequestBuilder().method("GET").session(sessionOwner).uri(routes.HomeController.homes(pTest.getName()).url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/PTest", request.uri());
        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("<a href=/EditHome><button type=\"submit\" id=\"bedit\"></button></a>"));
    }
    
    @Test
    public void testToGoEditHomeNotAdmin() {
    	
    	request = new Http.RequestBuilder().method("GET").uri(routes.HomeController.editHome().url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	
    }
    
    //POST    /EditHome         				controllers.HomeController.postEditHome
    @Test
    public void testEditHome() {
    	
    	Map<String,String> sessionOwner=new HashMap<String,String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("resumeEn", "New Text En");
        map.put("resumeFr", "Nouveau Texte francais");
        map.put("send", "send");
    	sessionOwner.put("project", PROJECT);
    	Session session=new Session(sessionOwner);
    	session.put("project", PROJECT);
        pTest=Project.find.byId(PROJECT);
        
        assertEquals("",pTest.getResumeEn());
        assertEquals("",pTest.getResumeFr());
        
    	request = new Http.RequestBuilder().method("POST").session(sessionOwner).uri(routes.HomeController.postEditHome().url()).bodyForm(map);
    	Result result = route(app,request);
    	
    	pTest=Project.find.byId(PROJECT);
        assertEquals("New Text En",pTest.getResumeEn());
        assertEquals("Nouveau Texte francais",pTest.getResumeFr());
    	assertEquals("This is not the page","/", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	
    }
    
	@After
	public void teardown() {
		
		ownerTest.removeProject(pTest);
		pTest.removeOwner(ownerTest);
		
		pTest.delete();
		ownerTest.delete();
		userOTest.delete();
		statustest.delete();
		stop(app);
	  
	}
    
}