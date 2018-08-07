package TestsModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Administrator;
import models.Users;
import play.test.WithApplication;

public class AdminTest extends WithApplication{
	
	private final String CIP = "PseudoTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userTest;
	private Administrator adminTest;
	private Integer adminTestId;
	
	@Before
	public void initTestAdmin() {
		
		int max;
		userTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userTest.save();
		adminTest = new Administrator(userTest);
		adminTest.save();
		
		if(Administrator.find.all().size() !=0 ){
			max =Administrator.find.all().get(Administrator.find.all().size() - 1).getIdAdmin();
			for(int i =0 ; i<Administrator.find.all().size();i++) {
				if(Administrator.find.all().get(i).getIdAdmin()>max) {
					max =Administrator.find.all().get(i).getIdAdmin();
				}
			}
			this.adminTestId = max;
		}else {
			adminTestId = 0;
		}
		
	}
	
	@Test
	public void instanceAdministratorsTest() {
		
		assertNotNull("The instance is not created", adminTest);
		
	}
	
	@Test
	public void userAdminInstaceTest() {
		
		assertEquals("Bad User in this instance",userTest,adminTest.getUserAdmin());
	
	}
	
	@Test 
	public void idAdminInstanceTest() {
		
		assertNotNull("This admin Don't have any id ",adminTest.getIdAdmin());
		
	}
	
	@Test
	public void tupleAdminTest(){
		assertNotNull("This admin hasn't insert into the data base", Administrator.find.byId(this.adminTestId));
	}
	
	@Test
	public void tupleAdminUserTest() {
		assertEquals("userTest is not Administrator",userTest,Administrator.find.byId(this.adminTestId).getUserAdmin());
	}
	
	@Test
	public void getAdministratorByCIPMethode() {
		assertEquals("userTest is not Administrator",userTest,Administrator.getAdministratorByCIP(CIP).getUserAdmin());
	}

	@After
	public void endTestAdmin() {
		adminTest.delete();
		adminTest=null;
		userTest.delete();
		userTest=null;

	}
}