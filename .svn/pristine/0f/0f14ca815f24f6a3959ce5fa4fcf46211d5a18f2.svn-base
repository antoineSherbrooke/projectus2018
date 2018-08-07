package controllers;

import models.Users;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static play.test.Helpers.*;

public class InitControllerTest extends InitController {
    private Http.RequestBuilder request;


    @Before
    public void before() {
        start(fakeApplication(inMemoryDatabase()));
        request = new Http.RequestBuilder();
        ConstantsTest.setHttpContext(request);
    }

    @Test
    public void testCreateSuperAdmin() {
        assertTrue("db should be empty at first launch", !superAdminExist());
        createSuperAdmin();
        assertTrue("a user \"superadmin\" should be created", superAdminExist());
        try {
            createSuperAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            fail("create superadmin twice shoud do nothing on the db");
        }
    }

    private Boolean superAdminExist() {
        return Users.find.where().eq("cip", "superadmin").findUnique() != null;
    }


}
