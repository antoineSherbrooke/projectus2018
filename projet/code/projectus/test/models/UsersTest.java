package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Members.EnumMem;
import models.Associations.EnumAsso;
import java.util.*;

import static org.junit.Assert.*;
import play.test.WithApplication;
import utils.PasswordStorage;

import static play.test.Helpers.*;

public class UsersTest extends WithApplication{

    private Users usersTest, user;
    private Members memberDev = new Members(true,EnumMem.DEVELOPER);
    private Members memberPO = new Members(false,EnumMem.PRODUCT_OWNER);
    private Members memberSM = new Members(false,EnumMem.SCRUM_MASTER);
    private Members memberAdmin = new Members(false,EnumMem.ADMINISTRATOR);
    private List<Associations> associationsList = new ArrayList();
    private List<Members> membersList = new ArrayList<>();
    private Projects projectTest;
    private Groups groupsTest;

    @Before
    public void before() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        user = new Users("MyCIP","MyFirstName","MyLastName","MyPassword");
        memberDev.setAssociations(new Associations(user,EnumAsso.MEMBER));
        associationsList.add(new Associations(user,EnumAsso.MEMBER,memberDev));
        associationsList.add(new Associations(user,EnumAsso.ADMINISTRATOR,memberAdmin));
        usersTest = new Users("MyCIP","MyFirstName","MyLastName","MyPassword");
        memberPO.setAssociations(new Associations(usersTest, Associations.EnumAsso.MEMBER));
        memberAdmin.setAssociations(new Associations(usersTest, Associations.EnumAsso.MEMBER));
        memberSM.setAssociations(new Associations(usersTest, Associations.EnumAsso.MEMBER));
        membersList.add(memberPO);
        membersList.add(memberAdmin);
        membersList.add(memberDev);
        membersList.add(memberSM);
        projectTest = new Projects("Description","Project Name",true);
        groupsTest = new Groups("Group Name", "Email", projectTest, membersList);
        for(Members listLoop : membersList){
            listLoop.setGroups(groupsTest);
            associationsList.add(new Associations(usersTest,EnumAsso.MEMBER,listLoop));
        }
        groupsTest.setMembers(membersList);
        usersTest.setAssociations(associationsList);
        projectTest.setGroups(groupsTest);
        projectTest.save();
    }

    @After
    public void after() throws Exception {
        usersTest = null;
        user = null;
        memberAdmin = null;
        memberSM = null;
        memberPO = null;
        memberDev = null;
        associationsList = null;
        projectTest = null;
    }

    @Test
    public void users() {
        assertNotNull("The instance is not created", usersTest);
    }

    @Test
    public void getCip() {
        assertEquals("The cip is wrong", "MyCIP", usersTest.getCip());
    }

    @Test
    public void setCip() {
        usersTest.setCip("MyCIP2");
        assertEquals("The cip is wrong", "MyCIP2", usersTest.getCip());
    }

    @Test
    public void getFirstName() {
        assertEquals("The first name is wrong", "MyFirstName", usersTest.getFirstName());
    }

    @Test
    public void setFirstName() {
        usersTest.setFirstName("MyFirstName2");
        assertEquals("The first name is wrong", "MyFirstName2", usersTest.getFirstName());
    }
    @Test
    public void getLastName() {
        assertEquals("The last name is wrong", "MyLastName", usersTest.getLastName());
    }

    @Test
    public void setLastName() {
        usersTest.setLastName("MyLastName2");
        assertEquals("The last name is wrong", "MyLastName2", usersTest.getLastName());
    }
    @Test
    public void getPassword() throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        assertTrue("The password is wrong", PasswordStorage.verifyPassword("MyPassword", usersTest.getHash()));
    }

    @Test
    public void setPassword() throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        usersTest.setPassword("MyPassword2");
        assertTrue("The password is wrong", PasswordStorage.verifyPassword("MyPassword2", usersTest.getHash()));
    }
    @Test
    public void getAssociations(){
        assertEquals("The associationsList is wrong", associationsList, usersTest.getAssociations());
    }

    @Test
    public void setAssociations() {
        usersTest.setAssociations(associationsList);
        assertEquals("The associationsList is wrong", associationsList, usersTest.getAssociations());
    }

    @Test
    public void getActive () {
        assertTrue("The user is active",user.getActive());
    }

    @Test
    public void setActive() {
        user.setActive(false);
        assertFalse("The user is non active", user.getActive());
    }

    @Test
    public void connect() {
        new Users("MyCIP","MyFirstName","MyLastName","MyPassword", associationsList).save();
        new Users("MyCIP2","MyFirstName","MyLastName","MyPassword", associationsList,false).save();
        assertEquals("The cip and the password is correct bus doesn't work", ConnectResult.OK,Users.connect("MyCIP","MyPassword"));
        assertNotSame("The password is correct bus doesn't work", ConnectResult.E_PASSWORD ,Users.connect("MyCIP","MyPassword"));
        assertNotSame("The cip is correct bus doesn't work", ConnectResult.E_CIP,Users.connect("MyCIP","MyPassword"));
        assertNotSame("The user is active but doesn't work",ConnectResult.E_INACTIVE,Users.connect("MyCIP","MyPassword"));

        assertEquals("The user is not active but it work",ConnectResult.E_INACTIVE,Users.connect("MyCIP2","MyPassword"));
        assertNotSame("The cip and the password is  not correct bus it work",ConnectResult.OK,Users.connect("MyCIPFalse","MyPasswordFalse"));
        assertEquals("The password is not correct but it work",ConnectResult.E_PASSWORD,Users.connect("MyCIP","MyPasswordFalse"));
        assertEquals("The cip is not correct but it work",ConnectResult.E_CIP,Users.connect("MyCIPFalse","MyPassword"));
    }

    @Test
    public void isAdminInProject() {
        assertTrue("The user is admin in the project",usersTest.isAdminInProject(projectTest));
        assertFalse("The user is admin in the project",user.isAdminInProject(projectTest));
    }

    @Test
    public void isScrumInProject() {
        assertTrue("The user is SM in the project",usersTest.isScrumInProject(projectTest));
        assertFalse("The user is SM in the project",user.isScrumInProject(projectTest));
    }

    @Test
    public void isOwnerInProject() {
        assertTrue("The user is PO in the project",usersTest.isOwnerInProject(projectTest));
        assertFalse("The user is PO in the project",user.isOwnerInProject(projectTest));
    }

    @Test
    public void newUser() {


        Users usersInOtherProject = new Users("bosh0666","My Name is Boss","Hugo Boss");
        Projects otherProject = new Projects("Desc","Name",true);
        Groups otherGroups = new Groups(otherProject,"Name");
        Members memberDevInOtherProject = new Members(true,EnumMem.DEVELOPER);
        Members memberPOInOtherProject = new Members(false,EnumMem.PRODUCT_OWNER);
        Members memberSMInOtherProject = new Members(false,EnumMem.SCRUM_MASTER);
        Members memberAdminInOtherProject = new Members(false,EnumMem.ADMINISTRATOR);
        List<Members> membersListInOtherProject = new ArrayList<>();
        membersListInOtherProject.add(memberDevInOtherProject);
        membersListInOtherProject.add(memberPOInOtherProject);
        membersListInOtherProject.add(memberSMInOtherProject);
        membersListInOtherProject.add(memberAdminInOtherProject);
        for (Members listLoop : membersListInOtherProject) {
            listLoop.setAssociations(new Associations(usersInOtherProject,EnumAsso.MEMBER,listLoop));
            listLoop.setGroups(otherGroups);
        }
        otherGroups.setMembers(membersListInOtherProject);
        otherProject.save();
        otherProject.setGroups(otherGroups);


        for(Members listLoop : membersList){
            listLoop.setGroups(groupsTest);
            associationsList.add(new Associations(user,EnumAsso.MEMBER,listLoop));
            associationsList.add(new Associations(usersInOtherProject,EnumAsso.MEMBER,listLoop));
        }
        user.setAssociations(associationsList);



        Users userNew;
        try {
            userNew = Users.newUser("abcd1234", "FirstName", "LastName", projectTest);
            assertEquals("Wrong user, new user", new Users("abcd1234", "FirstName", "LastName"), userNew);
        } catch (Exception e) {
            assert false;
        }
        try {
            userNew = Users.newUser(user.getCip(), user.getFirstName(), user.getLastName(),projectTest);
            assertEquals("Wrong user",user,userNew);
        } catch (Exception e) {
            assertEquals("Wrong exception, cip","The cip already exist in this project",e.getMessage());
        }
        try {
            userNew = Users.newUser(usersInOtherProject.getCip(), usersInOtherProject.getFirstName(), usersInOtherProject.getLastName(),projectTest);
            assertEquals("Wrong user",usersInOtherProject,userNew);
        } catch (Exception e) {
            assert false;
        }
        try {
            userNew = Users.newUser(usersInOtherProject.getCip(), usersInOtherProject.getFirstName()+"test", usersInOtherProject.getLastName(),projectTest);
            assertEquals("Wrong user",usersInOtherProject,userNew);
        } catch (Exception e) {
            assertEquals("Wrong exception, first name","The cip already exist in an other project but the first name is wrong",e.getMessage());
        }
        try {
            userNew = Users.newUser(usersInOtherProject.getCip(), usersInOtherProject.getFirstName(), usersInOtherProject.getLastName()/*+"test"*/,projectTest);
            assertEquals("Wrong user",usersInOtherProject,userNew);
        } catch (Exception e) {
            assertEquals("Wrong exception, last name","The cip already exist in an other project but the last name is wrong",e.getMessage());
        }
    }

}