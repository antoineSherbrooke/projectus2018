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


public class SessionControllerTest extends WithApplication {
    @Before
    public void before(){
        start(fakeApplication(inMemoryDatabase()));
        ConstantsTest.createSuperadmin("superadmin");
        ConstantsTest.createUser("test1111");
    }


    @Test
    public void loginCipNull() {
        Result result = route(createLoginRequest("mare2423", "foobar"));
        assertTrue("La session devrait afficher faux", !result.session().containsKey("connected"));
        assertTrue("Il devrait y'avoir un message d'erreur", result.flash().containsKey("connect"));
        assertEquals("La page devrait afficher cip invalid", "Invalid cip", result.flash().get("connect"));

    }

    @Test
    public void loginPasswordNull() {
        Result result = route(createLoginRequest("test1111", "salade"));
        assertTrue("La session devrait afficher faux", !result.session().containsKey("connected"));
        assertTrue("Il devrait y'avoir un message d'erreur", result.flash().containsKey("connect"));
        assertEquals("La page devrait afficher password invalid", "Invalid password", result.flash().get("connect"));

    }

    @Test
    public void loginInSuperAdmin() {
        Result result = route(createLoginRequest("superadmin", "foobar"));
        assertTrue("Il devrait pas y'avoir de message d'erreur", !result.flash().containsKey("connect"));
        assertEquals("La page n'est pas la bonne", "/projectus/superadministrators", result.redirectLocation().get());
    }

    @Test
    public void loginInMember() {
        Result result = route(createLoginRequest("test1111", "foobar"));
        assertTrue("Il devrait pas y'avoir de message d'erreur", !result.flash().containsKey("connect"));
        assertEquals("La page n'est pas la bonne", "/projectus/dashboard", result.redirectLocation().get());
    }

    private Http.RequestBuilder createLoginRequest(String login, String password) {
        Map<String, String> post = new HashMap<>();
        post.put("cip", login);
        post.put("password", password);

        return new Http.RequestBuilder().method("POST")
            .bodyForm(post)
            .uri(routes.SessionController.loginPost().url());
    }

}
