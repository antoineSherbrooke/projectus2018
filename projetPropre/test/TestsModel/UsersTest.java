package TestsModel;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import models.Users;
import play.test.WithApplication;


public class UsersTest extends WithApplication {
	
	private final String CIP = "PseudoTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userTest;
	
	
	@Before
	public void initUser() {
		
		userTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userTest.save();
		
	}
	
	@Test
	public void instanceUsersTest() {
		assertNotNull("The instance is not created", userTest);
	}
	
	@Test
	public void pseudoInstanceTest() {
		assertEquals("Bad Pseudo in this instance",CIP,userTest.getCip());
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
		assertNotNull("User not find in the database",Users.find.byId(CIP));
	}
	
	@Test
	public void tupleUserPseudoTest() {
		assertEquals("Bad Pseudo in the database",CIP,Users.find.byId(CIP).getCip());
	}
	
	@Test
	public void tupleUserPasswordTest() {
		assertEquals("Bad Password in the database",PASSWORD,Users.find.byId(CIP).getPassword());
	}
	
	@Test
	public void tupleUserFirstNameTest() {
		assertEquals("Bad FirstName in the database",FIRSTNAME,Users.find.byId(CIP).getFirstName());
	}
	
	@Test
	public void tupleUserLastNameTest() {
		assertEquals("Bad LastName in the database",LASTNAME,Users.find.byId(CIP).getLastName());
	}
	
	@Test
	public void tupleUserMailTest() {
		assertEquals("Bad mail in the database",MAIL,Users.find.byId(CIP).getMail());
	}
	
	@After
	public void endTestUsers() {
		
		userTest.delete();
		userTest=null;
		
	}
}
