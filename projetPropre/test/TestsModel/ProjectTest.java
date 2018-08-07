package TestsModel;



import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Owner;
import models.Project;
import models.Users;
import play.test.WithApplication;

public class ProjectTest extends WithApplication {

	private final String CIP = "PseudoTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private final String PROJECT = "ProjectTest";
	
	private Users userTest;
	private Owner ownerTest;
	private Project projectTest;
	
	@Before
	public void initProjectTest() {
		
		userTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userTest.save();
		ownerTest = new Owner(userTest);
		ownerTest.save();
		projectTest = new Project(PROJECT, ownerTest);
		projectTest.save();

	}
	
	@Test
	public void  projectInstaceTest() {
		assertNotNull("The instance is not created", projectTest);
	}
	
	@Test
	public void tupleProjectTest(){
		assertNotNull("This admin hasn't insert into the data base", Project.find.byId(PROJECT));
	}
	
	
	@After
	public void endProjectTest() {
		projectTest.delete();
		ownerTest.delete();
		userTest.delete();
		
	}
	
	
}
