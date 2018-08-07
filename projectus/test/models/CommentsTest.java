package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Members.EnumMem;
import models.SprintTasks.EnumState;



public class CommentsTest {

  private Comments comments;
  private Comments comments2;
  private Date date = new Date(120);
  private Date date2 = new Date(121);
  private Members members = new Members(true,EnumMem.DEVELOPER);
  private SprintTasks sprinttasks = new SprintTasks("MyName",EnumState.TODO,"MyDescription",8);
  private Members members2 = new Members(false,EnumMem.DEVELOPER);
  private SprintTasks sprinttasks2 = new SprintTasks("MyName2",EnumState.TODO,"MyDescription2",18);


  @Before
  public void before() throws Exception {
    comments = new Comments("MyTitle","MyContent",date,sprinttasks,members);
    comments2 = new Comments("MyTitle2","My Second Content", date2);
  }

  @After
  public void after() throws Exception {
    comments = null;
    comments2 = null;
  }
  
  @Test
  public void comments() {
    assertNotNull("The instance is not created", comments);
    assertNotNull("The instance is not created", comments2);
  }

  @Test
  public void getTitle() {
    assertEquals("The title is wrong", "MyTitle", comments.getTitle());
  }
  
  @Test
  public void setTitle() {
    comments.setTitle("MyTitle2");
    assertEquals("The title is wrong", "MyTitle2", comments.getTitle());
  }

  @Test
  public void getContent() {
    assertEquals("The content is wrong", "MyContent", comments.getContent());
  }

  @Test
  public void setContent() {
    comments.setContent("MyContent2");
    assertEquals("The content is wrong", "MyContent2", comments.getContent());
  }
  @Test
  public void getMembers() {
    assertEquals("The members is wrong", members, comments.getMembers());
  }

  @Test
  public void setMembers() {
    comments.setMembers(members2);
    assertEquals("The members is wrong", members2, comments.getMembers());
  }
  @Test
  public void getSprintTasks() {
    assertEquals("The sprint tasks is wrong", sprinttasks, comments.getSprintTasks());
  }

  @Test
  public void setSprintTasks() {
    comments.setSprintTasks(sprinttasks2);
    assertEquals("The sprint tasks is wrong", sprinttasks2, comments.getSprintTasks());
  }
  @Test
  public void getDate() {
    assertEquals("The date tasks is wrong", date, comments.getDate());
  }

  @Test
  public void setDate() {
    comments.setDate(date2);
    assertEquals("The date tasks is wrong", date2, comments.getDate());
  }
}