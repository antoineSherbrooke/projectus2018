package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import models.Members.EnumMem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


public class GroupsTest {

  private Groups groups;
  private Members memb = new Members(true,EnumMem.DEVELOPER);
  private Members memb2 = new Members(false,EnumMem.DEVELOPER);
  private Projects projects = new Projects("MyDescription","MyName",true);
  private Projects projects2 = new Projects("MyDescription2","MyName2",true);
  private List<Members> members = new ArrayList();
  private List<Members> members2 = new ArrayList();



  @Before
  public void before() throws Exception {
    members.add(memb);
    members2.add(memb2);
    groups = new Groups("MyName","MyEmail",projects,members);
  }

  @After
  public void after() throws Exception {
    groups = null;
  }
  
  @Test
  public void groups() {
    assertNotNull("The instance is not created", groups);
  }

  @Test
  public void getName() {
    assertEquals("The name is wrong", "MyName", groups.getName());
  }
  
  @Test
  public void setName() {
    groups.setName("MyName2");
    assertEquals("The name is wrong", "MyName2", groups.getName());
  }
   @Test
  public void getEmail() {
    assertEquals("The email is wrong", "MyEmail", groups.getEmail());
  }
  
  @Test
  public void setEmail() {
    groups.setEmail("MyEmail2");
    assertEquals("The email is wrong", "MyEmail2", groups.getEmail());
  }
  @Test
  public void getProjects() {
    assertEquals("The projects is wrong", projects, groups.getProjects());
  }
  
  @Test
  public void setProjects() {
    groups.setProjects(projects2);
    assertEquals("The projects is wrong", projects2, groups.getProjects());
  }
   @Test
  public void getMembers() {
    assertEquals("The members is wrong", members, groups.getMembers());
  }
  
  @Test
  public void setMembers() {
    groups.setMembers(members2);
    assertEquals("The members is wrong", members2, groups.getMembers());
  }
  
 
}