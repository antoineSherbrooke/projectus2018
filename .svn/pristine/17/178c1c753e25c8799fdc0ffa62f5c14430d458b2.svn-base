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

import controllers.ConstanteMessagesFlash;
import controllers.RegisterController;
import controllers.routes;
import models.Administrator;
import models.Status;
import models.Users;
import play.twirl.api.Content;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RegistersTest extends WithApplication{
	
	private final String CIP = "PseudoAdminTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userTest;
	private Users userATest;
	private Administrator adminTest;
	RequestBuilder request ;
	Application app;
	Http.Session session;
	Status statustest;

	
    @Before
    public void before() {

    	app = fakeApplication();
    	request = new Http.RequestBuilder();
	    start(app);
    	statustest=new Status("Test");
    	statustest.save();
    	
		userATest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userATest.save();
		adminTest = new Administrator(userATest);
		adminTest.save();
		

        
    }
    
    @Test
    public void testGoToRegiterNotAdmin() {
  
    	request = new Http.RequestBuilder().method("GET").uri(routes.RegisterController.registerForm().url());
        Result result = route(app,request);
        assertEquals(303, result.status());
        assertEquals("This is not the page","/", result.redirectLocation().get());
        
    }
    
    @Test
    public void testGoToRegiterAdmin() {
    	
    	Map<String,String> sessionadmin=new HashMap<String,String>();
    	sessionadmin.put("name", CIP);
    	Session session=new Session(sessionadmin);
    	session.put("name", CIP);
    	
    	request = new Http.RequestBuilder().method("GET").session(session).uri(routes.RegisterController.registerForm().url());

        Result result = route(app,request);
        assertEquals("This is not the page","/Register", request.uri());
        assertEquals(OK, result.status());

    	
    }
    
    @Test
    public void testAddUser(){
    	
        HashMap<String, String> map = new HashMap<>();
        map.put("cip", "Test");
        map.put("firstName", "Test");
        map.put("lastName", "Test");
        map.put("mail", "Test@gmail.com");
        map.put("password", "Test");
        map.put("status", "Test");
        map.put("role", "user");
  
    	request = new Http.RequestBuilder().method("POST").uri(routes.RegisterController.registerFormValidation().url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	assertNotNull(Users.find.byId("Test"));
        
    	
    }
    
    @Test
    public void testAddUserError(){

    	this.userTest = new Users("Test","Test","Test","Test","Test",statustest);
    	userTest.save();
        HashMap<String, String> map = new HashMap<>();
        map.put("cip", "Test");
        map.put("firstName", "Test");
        map.put("lastName", "Test");
        map.put("mail", "Test@gmail.com");
        map.put("password", "Test");
        map.put("status", "Test");
        map.put("role", "user");
  
    	request = new Http.RequestBuilder().method("POST").uri(routes.RegisterController.registerFormValidation().url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Register", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	

    		
    }
	@After
	public void teardown() {

		if(Users.find.byId("Test")!=null) {
			Users.find.byId("Test").delete();
		}
		adminTest.delete();
        userATest.delete();
		statustest.delete();
		stop(app);
	  
	}

}
