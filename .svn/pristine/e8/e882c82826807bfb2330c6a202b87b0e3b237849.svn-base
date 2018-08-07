package TestsModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Status;
import play.test.WithApplication;

public class StatusTest extends WithApplication{
	
	private final String NAME = "StatusTest";
	
	private Status statusTest;
	
	@Before
	public void initTestStatus() {

		this.statusTest = new Status(NAME);
		this.statusTest.save();
		
	}
	
	@Test
	public void instanceStatusTest() {
		assertNotNull("The instance is not created", statusTest);
	}
	
	@Test
	public void nameStatusInstanceTest() {
		assertEquals("Bad name in this instance of Status",NAME,this.statusTest.getName());
	}
	
	@Test
	public void tupleStatusTest() {
		assertEquals("The status is not in the data base",this.statusTest , Status.find.byId(NAME));
	}
	
	@After
	public void endTestStatus() {
		
		this.statusTest.delete();
		this.statusTest = null;
		
	}
	
}
