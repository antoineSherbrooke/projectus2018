package projetPropre;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.route;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.PasswordForgotController;
import controllers.routes;
import models.Administrator;
import models.LostPassword;
import models.Status;
import models.Users;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.test.WithApplication;
import static play.mvc.Http.Status.OK;


public class PasswordForgotTest extends WithApplication {

	private final String CIP = "PseudoAdminTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "test@hotmail.fr";
	private final String CODE = "testtesttesttesttest";
	
	private Users userTest;
	private LostPassword lp;
	RequestBuilder request ;
	Application app;
	Http.Session session;


	
    @Before
    public void before() {
    	app = fakeApplication();
    	request = new Http.RequestBuilder();
	    start(app);
    	userTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
    	userTest.save();
    	lp = new LostPassword(CODE, CIP);
    	lp.save();
    }
    
    @Test
    public void testChangePassword() {
    	HashMap<String, String> map = new HashMap<>();
        map.put("pass", "Testing");
        map.put("passVerif", "Testing");
    	request = new Http.RequestBuilder().method("POST").uri(routes.PasswordForgotController.changePassword(CODE).url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Login", result.redirectLocation().get());
    	assertEquals(303, result.status());
    	userTest = Users.find.byId(CIP);
    	assertEquals("Testing", userTest.getPassword());
    }
    
    @Test
    public void testSendMailResetPassword() {
    	HashMap<String, String> map = new HashMap<>();
        map.put("mail", MAIL);
    	request = new Http.RequestBuilder().method("POST").uri(routes.PasswordForgotController.sendMailResetPassword().url()).bodyForm(map);
    	Result result = route(app,request);
    	assertEquals("This is not the page","/Login", result.redirectLocation().get());
    	assertEquals(303, result.status());
        assertNotNull(LostPassword.findByCip(CIP));
    }
    
    @After
	public void teardown() {

		userTest.delete();
		lp.delete();
		stop(app);
	  
	}
}
