package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import models.SprintTasks.EnumState;
import models.Members.EnumMem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;


public class MembersTasksDoingTest {

  private MembersTasksDoing memberstasks;
  private Date date = new Date(120);
  private Date date2 = new Date(212);
  private Members members = new Members(true,EnumMem.DEVELOPER);
  private SprintTasks sprinttasks = new SprintTasks("MyName",EnumState.TODO,"MyDescription",8);
  private Members members2 = new Members(false,EnumMem.DEVELOPER);
  private SprintTasks sprinttasks2 = new SprintTasks("MyName2",EnumState.TODO,"MyDescription2",8);

  @Before
  public void before() throws Exception {
    memberstasks = new MembersTasksDoing(23,date,sprinttasks,members);
  }

  @After
  public void after() throws Exception {
    memberstasks = null;
  }
  
  @Test
  public void sprinttasks() {
    assertNotNull("The instance is not created", memberstasks);
  }

  @Test
  public void getName() {
    assertEquals("The time spent min is wrong",23, memberstasks.getTimeSpent());
  }
  
  @Test
  public void setTimeSpentMin() {
    memberstasks.setTimeSpent(24);
    assertEquals("The time spent time min is wrong",24, memberstasks.getTimeSpent());
  }
  @Test
  public void getDay() {
    assertEquals("The day is wrong",date, memberstasks.getDay());
  }
  
  @Test
  public void setDay() {
    memberstasks.setDay(date2);
    assertEquals("The day is wrong",date2, memberstasks.getDay());
  }

  @Test
  public void getMembers() {
    assertEquals("The members is wrong",members, memberstasks.getMembers());
  }
  
  @Test
  public void setMembers() {
    memberstasks.setMembers(members2);
    assertEquals("The members is wrong",members2, memberstasks.getMembers());
  }
  @Test
  public void getSprintTasks() {
    assertEquals("The sprintTasks is wrong",sprinttasks, memberstasks.getSprintTasks());
  }
  
  @Test
  public void setSprintTasks() {
    memberstasks.setSprintTasks(sprinttasks2);
    assertEquals("The sprinttask is wrong", sprinttasks2, memberstasks.getSprintTasks());
  }

 
}