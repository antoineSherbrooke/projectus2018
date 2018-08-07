package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Members.EnumMem;
import models.Associations.EnumAsso;

public class AssociationsTest {

  private Associations associations;
  private Associations assoTest;
  private Members member = new Members(true,EnumMem.DEVELOPER);
  private Users user = new Users("MyCIP","MyFirstName","MyLastName","MyPassword");
  private Users user2 = new Users("MyCIP2","MyFirstName2","MyLastName2","MyPassword2");

    
  @Before
  public void before() throws Exception {
    associations = new Associations(user,EnumAsso.MEMBER,member);
    assoTest = new Associations(user,EnumAsso.MEMBER);
  }

  @After
  public void after() throws Exception {
    associations = null;
    assoTest = null;
  }

  @Test
  public void sprinttasks() {
    assertNotNull("The instance is not created", associations);
  }

  @Test
  public void getUsers() {
    assertEquals("The user is wrong",user, associations.getUsers());
  }
  
  @Test
  public void setUsers() {
    associations.setUsers(user2);
    assertEquals("The user is wrong", user2, associations.getUsers());
  }
  
  @Test
  public void getAssociationType() {
    assertEquals("The associationType is wrong",EnumAsso.MEMBER, associations.getAssociationType());
  }
  
  @Test
  public void setAssociationType() {
    associations.setAssociationType(EnumAsso.ADMINISTRATOR);
    assertEquals("The associationType is wrong",EnumAsso.ADMINISTRATOR, associations.getAssociationType());
  }
  @Test
  public void getMembers() {
    assertEquals("The members is wrong", member, associations.getMembers());
  }
  
  @Test
  public void setMembers() {
    Members member2 = new Members(true,EnumMem.DEVELOPER);
    associations.setMembers(member2);
    assertEquals("The name is wrong",member2, associations.getMembers());
  }
 
 
}