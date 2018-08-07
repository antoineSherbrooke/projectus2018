package controllers;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ProductBacklogControllerTest extends WithApplication {

    private Http.Session session;
    private Http.RequestBuilder request;
    private ProductBacklogController productBacklogController;

    @Before
    public void before() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        new InitController().initTest();
        productBacklogController = new ProductBacklogController();
        session = ConstantsTest.createSession("2","foobar");
    }
    @Test
    public void indexTest() {
        request = ConstantsTest.getRequest("GET",session,routes.ProductBacklogController.index().url());
        Result result = route(request);
        assertEquals("page uri is incorrect, not product_backlog","/projectus/product_backlog", request.uri());
        assertEquals("page should work", OK, result.status());
    }

    /*@Test
    public void addFeatureTest() {
        productBacklogController.addFeature()
        Map<String, String> map = new HashMap<String, String>();
        map.put("featureId", "99");
        map.put("featureName", "la feature de test");
        map.put("featurePriority", );
        map.put("featureDuration");
        map.put("featureDesc");
        request = ConstantsTest.getRequest("POST",session,routes.DashboardController.infoTask().url(),map);
        Result result = route(request);
    }*/

}
