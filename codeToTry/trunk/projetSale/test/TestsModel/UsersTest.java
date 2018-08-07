package TestsModel;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Users;
import play.test.WithApplication;


public class UsersTest extends WithApplication {
	
	private final String PSEUDO = "PseudoTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userTest;
	
	
	@Before
	public void initUser() {
		userTest = new Users(PSEUDO,PASSWORD,FIRSTNAME,LASTNAME,MAIL);
		userTest.save();
	}
	
	@Test
	public void instanceUsersTest() {
		assertNotNull("The instance is not created", userTest);
	}
	
	@Test
	public void pseudoInstanceTest() {
		assertEquals("Bad Pseudo in this instance",PSEUDO,userTest.getPseudo());
	}
	
	@Test
	public void passwordInstanceTest() {
		assertEquals("Bad Password in this instance",PASSWORD, userTest.getPassword());
	}
	
	@Test
	public void firstNameInstanceTest() {
		assertEquals("Bad FirstName in this instance",FIRSTNAME, userTest.getFirstName());
	}
	
	@Test
	public void lastNameInstanceTest() {
		assertEquals("Bad LastName in this instance",LASTNAME, userTest.getLastName());
	}
	
	@Test
	public void mailInstanceTest() {
		assertEquals("Bad Mail in this instance",MAIL, userTest.getMail());
	}
	
	@Test
	public void tupleUserTest() {
		assertNotNull("User not find in the database",Users.find.byId(PSEUDO));
	}
	
	@Test
	public void tupleUserPseudoTest() {
		assertEquals("Bad Pseudo in the database",PSEUDO,Users.find.byId(PSEUDO).getPseudo());
	}
	
	@Test
	public void tupleUserPasswordTest() {
		assertEquals("Bad Password in the database",PASSWORD,Users.find.byId(PSEUDO).getPassword());
	}
	
	@Test
	public void tupleUserFirstNameTest() {
		assertEquals("Bad FirstName in the database",FIRSTNAME,Users.find.byId(PSEUDO).getFirstName());
	}
	
	@Test
	public void tupleUserLastNameTest() {
		assertEquals("Bad LastName in the database",LASTNAME,Users.find.byId(PSEUDO).getLastName());
	}
	
	@Test
	public void tupleUserMailTest() {
		assertEquals("Bad mail in the database",MAIL,Users.find.byId(PSEUDO).getMail());
	}
	
	@After
	public void endTestUsers() {
		userTest=null;
		Users.find.byId(PSEUDO).delete();
	}
}
