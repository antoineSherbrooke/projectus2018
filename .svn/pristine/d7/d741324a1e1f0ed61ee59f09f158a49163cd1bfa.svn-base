
package projetPropre;


import static org.junit.Assert.*;

import play.Application;
import play.mvc.Http;
import play.mvc.Http.RequestBuilder;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.routes;
import models.Status;
import models.Users;
import play.twirl.api.Content;
import static play.test.Helpers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoginTest  extends WithApplication {
	
	private final String CIP = "PseudoAdminTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	private final String STATUS = "Test";
	private final String USERO = "user";
	
	
	private Users userOTest;
	Status statustest;
	RequestBuilder request ;
	Application app;
	Http.Session session;
	
    @Before
    public void before() {
    	
    	statustest=new Status(STATUS);
    	statustest.save();
    	userOTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
    	userOTest.save();
    	app = fakeApplication();
        request = new Http.RequestBuilder();
        start(app);
        
    }
    
    @Test
    public void testGoToLogin() {
    	
    	request = new Http.RequestBuilder().method("GET").uri(routes.LoginController.loginForm().url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Login", request.uri());
        assertEquals(OK, result.status());
        
    }
    
    @Test
    public void testGoToLoginAlreadyCo() {
    	
    	Map<String,String> sessionOwner=new HashMap<String,String>();
    	sessionOwner.put("name", CIP);
    	sessionOwner.put("user", USERO);
    	Session session=new Session(sessionOwner);
    	session.put("name", CIP);
    	session.put("user", USERO);

    	request = new Http.RequestBuilder().method("GET").session(sessionOwner).uri(routes.LoginController.loginForm().url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/", result.redirectLocation().get());
        assertEquals(303, result.status());
    }
    
    @Test
    public void testConnectionOk() {
    	
        HashMap<String, String> map = new HashMap<>();
        map.put("cip", "PseudoAdminTest");
        map.put("password", "PasswordTest");
  
    	request = new Http.RequestBuilder().method("POST").uri(routes.LoginController.loginFormValidation().url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/", result.redirectLocation().get());
    	assertEquals(303, result.status());
    }
	
    @Test
    public void testConnectionNotOk() {
    	
        HashMap<String, String> map = new HashMap<>();
        map.put("cip", "PseudoAdminTest");
        map.put("password", "oui");
  
    	request = new Http.RequestBuilder().method("POST").uri(routes.LoginController.loginFormValidation().url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Login", result.redirectLocation().get());
    	assertEquals(303, result.status());
    }
    
    @Test
    public void testDeconnection() {
    	request = new Http.RequestBuilder().method("GET").uri(routes.LoginController.disconnection().url());
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Login", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	
    }
    
    
	@After
	public void teardown() {
		
		userOTest.delete();
		statustest.delete();
		stop(app);
	  
	}

}
