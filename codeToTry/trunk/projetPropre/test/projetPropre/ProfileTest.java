package projetPropre;

import static org.assertj.core.api.Assertions.assertThat;
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

import controllers.RegisterController;
import controllers.routes;
import models.Users;
import models.Administrator;
import models.Owner;
import models.Status;
import play.twirl.api.Content;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class ProfileTest  extends WithApplication {
	
	RequestBuilder request ;
	Application app;
	
	private final String CIP = "PseudoTest";
	private final String CIPA = "PseudoATest";
	private final String CIPO = "PseudoOTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FirstNameTest";
	private final String LASTNAME = "LastNameTest";
	private final String MAIL = "MailTest";
	private final String MAILA = "MailTestA";
	private final String MAILO = "MailTestO";
	private final String RESUME_EN = "Welcome";
	private final String RESUME_FR = "Bienvenue";
	private final String STATUS = "StatusTest";
	private final String NEW_STATUS = "NEWStatusTest";
	
	private Status status; 
	private Status newStatus; 
	
	private Users userTest;
	private Users userATest;
	private Users userOTest;
	private Administrator adminTest;
	private Owner ownerTest;
	
	@Before
	public void initUser() {
		
		status = new Status(STATUS);
		status.save();
		newStatus = new Status(NEW_STATUS);
		newStatus.save();
		userTest=new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,status);
		userTest.save();
		userATest=new Users(CIPA,PASSWORD,FIRSTNAME,LASTNAME,MAILA,status);
		userATest.save();
		userOTest=new Users(CIPO,PASSWORD,FIRSTNAME,LASTNAME,MAILO,status);
		userOTest.save();
		adminTest = new Administrator(userATest);
		adminTest.save();
		ownerTest = new Owner(userOTest);
		ownerTest.save();
		
    	app = fakeApplication();
        request = new Http.RequestBuilder();
        start(app);
        
    }
    
    
    @Test
    public void testGoToProfileNotAdminOrProfileOwner() {
    	
    	request = new Http.RequestBuilder().method("GET").uri(routes.ProfileController.profile(CIP).url());
        Result result = route(app,request);
        assertEquals(200, result.status());
        assertEquals("This is not the page","/Profile/"+CIP, request.uri());
        
    }
    
    @Test
    public void testGoToProfileAsAdmin() {
    	Map<String,String> sessionadmin=new HashMap<String,String>();
    	sessionadmin.put("user", "admin");
    	sessionadmin.put("name", CIPA);
    	sessionadmin.put("language", "french");
    	Session session=new Session(sessionadmin);
    	session.put("user", "admin");
    	session.put("language", "french");
    	request = new Http.RequestBuilder().method("GET").session(session).uri(routes.ProfileController.profile(CIP).url());

        Result result = route(app,request);
        assertEquals("This is not the page","/Profile/"+ CIP, request.uri());
        assertEquals(OK, result.status());
     
        
    }
    
    @Test
    public void testGoToProfileAsOwner() {
    	
    	Map<String,String> sessionadmin=new HashMap<String,String>();
    	sessionadmin.put("user", "owner");
    	sessionadmin.put("name", CIPO);
    	sessionadmin.put("language", "french");
    	Session session=new Session(sessionadmin);
    	session.put("user", "owner");
    	session.put("language", "french");
    	
    	request = new Http.RequestBuilder().method("GET").session(session).uri(routes.ProfileController.profile(CIP).url());
        Result result = route(app,request);
        
        assertEquals("This is not the page","/Profile/"+ CIP, request.uri());
        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("<a href=/EditProfile/PseudoTest><button type=\"submit\" id=\"bedit\">"));
        
    }
	

    @Test
    public void testGoToEditMyProfil() {
    	Map<String,String> sessionadmin=new HashMap<String,String>();
    	sessionadmin.put("user", "user");
    	sessionadmin.put("name", CIP);
    	sessionadmin.put("language", "french");
    	Session session=new Session(sessionadmin);
    	session.put("user", "owner");
    	session.put("language", "french");
    	
    	request = new Http.RequestBuilder().method("GET").session(session).uri(routes.ProfileController.editProfile(CIP).url());
        Result result = route(app,request);
        
        assertEquals("This is not the page","/EditProfile/"+ CIP, request.uri());
        assertEquals(OK, result.status());
    }
    
    @Test
    public void testGoToEditNotProfil() {
    	Map<String,String> sessionadmin=new HashMap<String,String>();
    	sessionadmin.put("user", "user");
    	sessionadmin.put("name", CIP);
    	sessionadmin.put("language", "french");
    	Session session=new Session(sessionadmin);
    	session.put("user", "user");
    	session.put("language", "french");
    	
    	request = new Http.RequestBuilder().method("GET").session(sessionadmin).uri(routes.ProfileController.editProfile(CIPA).url());
        Result result = route(app,request);

        assertEquals("This is not the page","/Profile/"+CIPA, result.redirectLocation().get());
        assertEquals(303, result.status());
    }  
    
    @Test
    public void testModifyMyProfilValidWithPassWord() {
    	Result result;
        HashMap<String, String> map = new HashMap<>();
        
        map.put("send", "send");
        map.put("firstname", "newfirstnametest");
        map.put("lastname", "newlastnametest");
        map.put("mail", "mailtest2@gmail.com");
        map.put("resumeEn", "resumeTestEn");
        map.put("resumeFr", "resumeTestFr");
        map.put("password", "NewPasswordTest");
        map.put("verifPassword", "NewPasswordTest");
    	
    	request = new Http.RequestBuilder().method("POST").uri(routes.ProfileController.editMyProfileFormValidation(CIP).url()).bodyForm(map);
        result = route(app,request);
        
        userTest=Users.find.byId(CIP);
        
        assertEquals(userTest.getFirstName(),"newfirstnametest");
        assertEquals(userTest.getLastName(),"newlastnametest");
        assertEquals(userTest.getMail(),"mailtest2@gmail.com");
        assertEquals(userTest.getResumeEn(),"resumeTestEn");
        assertEquals(userTest.getResumeFr(),"resumeTestFr");
        assertEquals(userTest.getPassword(),"NewPasswordTest");
        assertEquals("This is not the page","/Profile/"+CIP, result.redirectLocation().get());
        assertEquals(303, result.status());
    	
    }
    
    @Test
    public void testModifyProfilValidWithPassWord() {
    	Result result;
        HashMap<String, String> map = new HashMap<>();
        
        map.put("send", "send");
        map.put("firstname", "newfirstnametest");
        map.put("lastname", "newlastnametest");
        map.put("mail", "mailtest2@gmail.com");
        map.put("resumeEn", "resumeTestEn");
        map.put("resumeFr", "resumeTestFr");
        map.put("password", "NewPasswordTest");
        map.put("verifPassword", "NewPasswordTest");
        map.put("status",NEW_STATUS );
        
    	request = new Http.RequestBuilder().method("POST").uri(routes.ProfileController.editProfileFormValidation(CIP).url()).bodyForm(map);
        result = route(app,request);
        
        userTest=Users.find.byId(CIP);
        
        assertEquals(userTest.getFirstName(),"newfirstnametest");
        assertEquals(userTest.getLastName(),"newlastnametest");
        assertEquals(userTest.getMail(),"mailtest2@gmail.com");
        assertEquals(userTest.getResumeEn(),"resumeTestEn");
        assertEquals(userTest.getResumeFr(),"resumeTestFr");
        assertEquals(userTest.getPassword(),"NewPasswordTest");
        assertEquals(userTest.getStatus().getName(),NEW_STATUS);
        assertEquals("This is not the page","/Profile/"+CIP, result.redirectLocation().get());
        assertEquals(303, result.status());
    	
    }
    @Test
    public void testModifyMyProfilCancel() {
    	Result result;
        HashMap<String, String> map = new HashMap<>();
        
        map.put("send", "cancel");
        userTest=Users.find.byId(CIP);
        
    	request = new Http.RequestBuilder().method("POST").uri(routes.ProfileController.editMyProfileFormValidation(CIP).url()).bodyForm(map);
        result = route(app,request);
        
        assertEquals(userTest.getFirstName(),FIRSTNAME);
        assertEquals(userTest.getLastName(),LASTNAME);
        assertEquals(userTest.getMail(),MAIL);
        assertEquals(userTest.getResumeEn(),null);
        assertEquals(userTest.getResumeFr(),null);
        assertEquals(userTest.getPassword(),PASSWORD);
        assertEquals("This is not the page","/Profile/"+CIP, result.redirectLocation().get());
        assertEquals(303, result.status());
    	
    }
    @After
	public void teardown() {
		Administrator.getAdministratorByCIP(CIPA).delete();
		Owner.getOwnerByCIP(CIPO).delete();
		Users.find.deleteById(CIPA);
		Users.find.deleteById(CIPO);
		Users.find.deleteById(CIP);
		Status.find.deleteById(STATUS);
		Status.find.deleteById(NEW_STATUS);
		stop(app);
	  
	}

}

