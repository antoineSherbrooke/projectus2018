package TestsModel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Owner;
import models.Users;
import play.test.WithApplication;

public class OwnerTest extends WithApplication{

	private final String CIP = "PseudoTest";
	private final String PASSWORD = "PasswordTest";
	private final String FIRSTNAME = "FisteNameTest";
	private final String LASTNAME = "LasteNameTest";
	private final String MAIL = "MailTest";
	
	private Users userTest;
	private Owner ownerTest;
	private Integer ownerTestId;
	
	@Before
	public void initTestAdmin() {
		
		int max;
			
		userTest = new Users(CIP,PASSWORD,FIRSTNAME,LASTNAME,MAIL,null);
		userTest.save();
		ownerTest = new Owner(userTest);
		ownerTest.save();
		
		if(Owner.find.all().size() !=0 ){
			max =Owner.find.all().get(Owner.find.all().size() - 1).getIdOwner();
			for(int i =0 ; i<Owner.find.all().size();i++) {
				if(Owner.find.all().get(i).getIdOwner()>max) {
					max =Owner.find.all().get(i).getIdOwner();
				}
			}
			this.ownerTestId = max;
		}else {
			ownerTestId = 0;
		}
		
		
		
	}
	
	@Test
	public void instanceAdministratorsTest() {
		
		assertNotNull("The instance is not created", ownerTest);
		
	}
	
	@Test
	public void userAdminInstaceTest() {
		
		assertEquals("Bad User in this instance",userTest,ownerTest.getuserOwner());
	
	}
	
	@Test 
	public void idAdminInstanceTest() {
		
		assertNotNull("This admin Don't have any id ",ownerTest.getIdOwner());
		
	}
	
	@Test
	public void tupleAdminTest(){
		assertNotNull("This admin hasn't insert into the data base", Owner.find.byId(this.ownerTestId));
	}
	
	@Test
	public void tupleAdminUserTest() {
		assertEquals("userTest is not Administrator",userTest,Owner.find.byId(this.ownerTestId).getuserOwner());
	}
	
	@Test
	public void getAdministratorByCIPMethode() {
		assertEquals("userTest is not Administrator",userTest,Owner.getOwnerByCIP(CIP).getuserOwner());
	}

	@After
	public void endTestAdmin() {
		ownerTest.delete();
		ownerTest=null;
		userTest.delete();
		userTest=null;
	}
}
