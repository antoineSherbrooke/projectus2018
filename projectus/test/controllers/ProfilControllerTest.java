package controllers;

import models.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsString;

public class ProfilControllerTest {

    private Http.Session session;
    private Http.Session session2;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();

    @Before
    public void before(){
        start(ConstantsTest.fakeApp());
        new InitController().initTest();
        ConstantsTest.createUser("test1111");
        session = ConstantsTest.createSession("test1111", "foobar");
        Users users = Users.find.where().eq("cip", "2").setMaxRows(1).findUnique();
        users.setHash("");
        users.update();
        session2 = ConstantsTest.createSession("2", "foobar");
        request = new Http.RequestBuilder();
        map.put("firstName","MyFirstName");
        map.put("lastName","MyLastName");
        map.put("password","MyPassword");
        map.put("passwordValid","MyPassword");


    }

    @After
    public void cleanup(){}

    @Test
    public void index() {
        request = ConstantsTest.getRequest("GET",session,routes.ProfilController.index().url());
        Result result = route(request);
        assertEquals("This is not the page profil","/projectus/profil", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }


    @Test
    public void firstLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("cip", "2");
        map.put("password", "");
        request = ConstantsTest.getRequest("POST",session2,routes.SessionController.loginPost().url(), map);
        Result result = route(request);
        assertEquals("The page should be work", ConstantsTest.REDIRECTION, result.status());
        assertTrue("The content of the page is false", contentAsString(result).contains("First connexion"));
        assertEquals("The page is not good", "/projectus/firstLogin", result.redirectLocation().get());

    }

    @Test
    public void showImageDark() {
        request = ConstantsTest.getRequest("GET",session,routes.ProfilController.showImageDark().url());
        Result result = route(request);
        assertEquals("This is not the page showImageDark","/projectus/profil/logo/dark", request.uri());
        //ERROR 303 SEE OTHER
        assertEquals("The page should be work", OK, result.status());
    }
    @Test
    public void showImageLight() {
        request = ConstantsTest.getRequest("GET",session,routes.ProfilController.showImageLight().url());
        Result result = route(request);
        assertEquals("This is not the page showImageLight","/projectus/profil/logo/light", request.uri());
        //ERROR 303 SEE OTHER
        assertEquals("The page should be work", OK, result.status());
    }
    @Test
    public void editProfil() {
        request = ConstantsTest.getRequest("POST",session,routes.ProfilController.editProfil().url(),map);
        Result result = route(request);
        assertEquals("This is not the page editProfil","/projectus/profil/editProfil", request.uri());
        //ERROR 303 SEE OTHER
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void showImageLogo() {
        request = ConstantsTest.getRequest("GET",session,routes.ProfilController.showImageLogo("test1111").url());
        Result result = route(request);
        assertEquals("This is not the page showImageLogo","/projectus/profil/logo/test1111", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void changePassword() {
        request = ConstantsTest.getRequest("POST",session,routes.ProfilController.changePassword().url());
        Result result = route(request);
        assertEquals("This is not the page profil","/projectus/profil/edit/password", request.uri());
        //ERROR 400
        assertEquals("The page should be work", OK, result.status());
    }



}
