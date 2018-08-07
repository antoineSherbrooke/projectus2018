import models.Users;
import models.LostPassword;
import play.Application;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.twirl.api.Content;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import play.mvc.Http.RequestBuilder;
import play.test.Helpers;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import org.junit.Before;
import org.junit.Test;

import static play.test.Helpers.*;
import java.io.File;

public class NewPassTest extends WithApplication {
	private final String Newpassword="lilou";
	private final String Password="julien";
	private final String PSEUDO = "PseudoTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userPassword;
	@Before
	public void before() {
		userPassword=new Users(PSEUDO,Password,FIRSTNAME,LASTNAME,MAIL);
	}
	/*RequestBuilder request ;
	@Test
	public void renderforget() {

    	request = Helpers.fakeRequest().method(GET).uri("/MailController/forget");
        assertFalse("This is not the page forget",request.uri().equals("url which make no sense"));
        assertEquals("This is the page forget","/MailController/forget", request.uri());
        
        Content html = views.html.PasswordForfgot.render();
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("Forget"));
	}*/
	
	@Test
	public void instanceUsersPasswordTest() {
		assertNotNull("The instance is not created",userPassword);
		
	}
	@Test
	public void testPassword() {
		assertEquals(Password,userPassword.getPassword());
	}
	@Test
	public void testNewPassword() {
		userPassword.setPassword(Newpassword);
		userPassword.save();
		assertEquals(Newpassword,userPassword.getPassword());
	}
	
	

}
