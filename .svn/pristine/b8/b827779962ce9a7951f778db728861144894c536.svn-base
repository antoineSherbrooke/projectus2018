package TestsModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.LostPassword;
import play.test.WithApplication;

public class LostPasswordTest extends WithApplication{
	private final String TOKEN ="TokenTest";
	private final String PSEUDO = "PseudoTest";
	private final Calendar DATEOFLAPSE= Calendar.getInstance();
	
	LostPassword lostPassWordTest;
	
	@Before
	public void initLostPassword() {
		
		if(LostPassword.find.byId(TOKEN)!=null) {
			lostPassWordTest = LostPassword.find.byId(TOKEN);
			System.out.print("Tuple already in the database ");
		}else {
			lostPassWordTest = new LostPassword(TOKEN,PSEUDO,DATEOFLAPSE);
			lostPassWordTest.save();
			System.out.print("Tuple add to the database ");
		}

	}
	
	@Test
	public void instanceLostPasswordTest() {
		assertNotNull("The instance is not created", lostPassWordTest);
	}
	
	@Test
	public void tokenInstanceTest() {
		assertEquals("Bad Token in this instance",TOKEN,lostPassWordTest.getToken());
	}
	
	@Test
	public void pseudoInstanceTest() {
		assertEquals("Bad Pseudo in this instance",PSEUDO, lostPassWordTest.getCip());
	}
	
	@Test
	public void dateOfLapseInstanceTest() {
		assertEquals("Bad DateOfLapse in this instance",DATEOFLAPSE, lostPassWordTest.getDateOfLapse());
	}
	
	@Test
	public void tupleLostPasswordTest() {
		assertNotNull("LostPassword not find in the database",LostPassword.find.byId(TOKEN));
	}
	
	@Test
	public void tupleLostPasswordTokenTest() {
		assertEquals("Bad Token in the database",TOKEN,LostPassword.find.byId(TOKEN).getToken());
	}
	
	@Test
	public void tupleLostPasswordPseudoTest() {
		assertEquals("Bad Pseudo in the database",PSEUDO, LostPassword.find.byId(TOKEN).getCip());
	}
	
	@Test
	public void tupleLostPasswordDateOfLapseTest() {
		assertEquals("Bad DateOfLapse in the database",DATEOFLAPSE, LostPassword.find.byId(TOKEN).getDateOfLapse());
	}
	
	@After
	public void endTestLostPasswordTest() {
		lostPassWordTest=null;
		LostPassword.find.byId(TOKEN).delete();
	}
}
