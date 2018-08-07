import static org.junit.Assert.*;
import play.mvc.Http.RequestBuilder;
import play.test.Helpers;
import play.test.WithApplication;
import org.junit.Test;
import play.twirl.api.Content;
import static play.test.Helpers.*;
import java.io.File;

public class LoginTest  extends WithApplication {
	
	RequestBuilder request ;
	
    @Test
    public void testGoToLogin() {
    	
    	request = Helpers.fakeRequest().method(GET).uri("/LoginController/formulaire");
        assertFalse("This is not the page Login",request.uri().equals("url which make no sense"));
        assertEquals("This is the page Login","/LoginController/formulaire", request.uri());
        
        Content html = views.html.Login.render("Login");
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("Formulaire"));
    }
    
	
	@Test
	public void testExistanceLectureFichierLogin() {
		File java = new File("app/controllers/LoginController.java");
		assertTrue(java.exists());
		File lecjava = new File("app/controllers/LoginController.java");
		assertTrue(lecjava.canRead());
		File scala = new File("app/views/Login.scala.html");
		assertTrue(scala.exists());
		File lecscala = new File("app/views/Login.scala.html");
		assertTrue(lecscala.canRead());
	}
	



}
