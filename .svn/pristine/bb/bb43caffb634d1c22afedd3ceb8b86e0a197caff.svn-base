package controllers;


import org.junit.*;
import static org.junit.Assert.*;

import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class HomeControllerTest extends WithApplication{



  @Test
  public void index(){
      Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
              .uri(routes.HomeController.index().url());
      Result result = route(request);
      assertEquals("This is not the page","/projectus", request.uri());
      assertEquals("The page should be work", OK, result.status());
      assertTrue("The content of the page is not good", contentAsString(result).contains("Welcome to ProjectUS"));
  }

}