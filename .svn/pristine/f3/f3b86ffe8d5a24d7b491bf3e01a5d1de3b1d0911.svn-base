package TestsModel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Owner;
import models.Project;
import models.ProjectUserParticipation;
import models.Users;
import play.test.WithApplication;

public class ProjectUserParticipationTest extends WithApplication{
	private final String CIPUSER = "PseudoTest";
	private final String CIPOWNER = "PseudoOwnerTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	private final String MAILOWNER = "MailOWnerTest";
	
	private final String NAMEPROJECT = "ProjectTest";
	private final String RESUMEPROJECT = "Resume projectTest";
	private final String RESUMEPROJECT_FR = "French Resume projectTest";
	
	private Users userTest;
	private Users userOwnerTest;
	private Owner ownerTest;
	private Project projectTest;
	private ProjectUserParticipation participationTest;
	private int participationTestId;

	
	@Before
	public void initTestParticipation() {
		
		userTest = new Users(CIPUSER,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userTest.save();
		userOwnerTest = new Users(CIPOWNER,PASSWORD,FIRSTNAME,LASTNAME,MAILOWNER,null);
		userOwnerTest.save();
		ownerTest = new Owner(userOwnerTest);
		ownerTest.save();
		
		projectTest = new Project(NAMEPROJECT,RESUMEPROJECT,RESUMEPROJECT_FR,this.ownerTest);
		projectTest.save();
		
		this.participationTest = new ProjectUserParticipation(userTest.getCip(),projectTest.getName());
		this.participationTest.save();
		
		participationTestId = this.participationTest.getId();
		
	}
	
	@Test
	public void instanceParticipationTest() {
		
		assertNotNull("The instance is not created", participationTest);
		
	}
	
	@Test
	public void instanceUserAdminInstaceTest() {
		
		assertEquals("Bad User in this instance",userTest,participationTest.getUserPaticipation());
	
	}
	
	@Test
	public void instanceProjectAdminInstaceTest() {
		
		assertEquals("Bad User in this instance",this.projectTest,participationTest.getProjectPaticipation());
	
	}
	
	@Test
	public void instanceActivityAdminInstaceTest() {
		
		assertEquals("Bad User in this instance",1,participationTest.getActivity());
		participationTest.offActivity();
		assertEquals("changes not affect",0,participationTest.getActivity());		
		participationTest.inActivity();
		assertEquals("changes not affect",1,participationTest.getActivity());

	
	}
	
	@Test
	public void tupleParticipationTest() {
		
		assertNotNull("The instance is not created", ProjectUserParticipation.find.byId(this.participationTestId));
		
	}
	
	@Test
	public void tupleUserAdminInstaceTest() {
		
		Users usertesttuple = ProjectUserParticipation.find.byId(this.participationTestId).getUserPaticipation();
		assertEquals("Bad User in this instance",userTest,usertesttuple);
	
	}
	
	@Test
	public void tupleProjectAdminInstaceTest() {
		Project projecttesttuple = ProjectUserParticipation.find.byId(this.participationTestId).getProjectPaticipation();
		assertEquals("Bad User in this instance",this.projectTest,projecttesttuple);
	
	}
	
	@Test
	public void tupleActivityAdminInstaceTest() {
		
		
		assertEquals("Bad User in this instance",1,ProjectUserParticipation.find.byId(this.participationTestId).getActivity());
		participationTest.offActivity();
		participationTest.save();
		assertEquals("changes not affect",0,ProjectUserParticipation.find.byId(this.participationTestId).getActivity());		
		participationTest.inActivity();
		participationTest.save();
		assertEquals("changes not affect",1,ProjectUserParticipation.find.byId(this.participationTestId).getActivity());

	}	
	
	@After
	public void endTestParticipation() {
		
		participationTest.delete();
		projectTest.delete();
		ownerTest.delete();
		userTest.delete();
		userOwnerTest.delete();

		userTest=null;
		userOwnerTest=null;
		ownerTest=null;
		projectTest=null;
		participationTest=null;
	}
	
}
