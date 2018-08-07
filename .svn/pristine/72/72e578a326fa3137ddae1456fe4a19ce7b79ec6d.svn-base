package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Members.EnumMem;


import java.util.*;

import models.Associations.EnumAsso;


public class MembersTest {

    private Members members;
    private Date date = new Date(120);
    private Date date2 = new Date(212);
    private MembersTasksDoing met = new MembersTasksDoing(23,date);
    private MembersTasksDoing met2 = new MembersTasksDoing(23,date);
    private Members member = new Members(true,EnumMem.DEVELOPER);
    private Members member2 = new Members(false,EnumMem.PRODUCT_OWNER);
    private Users user = new Users("MyCIP","MyFirstName","MyLastName","MyPassword");
    private Users user2 = new Users("MyCIP2","MyFirstName2","MyLastName2","MyPassword2");
    private Associations associations = new Associations(user,EnumAsso.MEMBER,member);
    private Associations associations2 = new Associations(user2,EnumAsso.MEMBER,member2);
    private Groups groups = new Groups("MyName","MyEmail");
    private Groups groups2 = new Groups("MyName2","MyEmail2");
    private Comments co = new Comments("MyTitle","MyContent",date);
    private Comments co2 = new Comments("MyTitle2","MyContent2",date2);
    private List<MembersTasksDoing> membersTaskDoings = new ArrayList();
    private List<MembersTasksDoing> membersTasksDoing2 = new ArrayList();
    private List<Comments> comments = new ArrayList();
    private List<Comments> comments2 = new ArrayList();

    @Before
        public void before() throws Exception {
        membersTaskDoings.add(met);
        membersTaskDoings.add(met2);
        comments.add(co);
        comments.add(co2);
        members = new Members(true,EnumMem.DEVELOPER,associations,groups,comments, membersTaskDoings);
    }

    @After
        public void after() throws Exception {
        members = null;
    }

    @Test
    public void members() {
        assertNotNull("The instance is not created", members);
    }

    @Test
    public void getNews() {
        assertEquals("The news is wrong", true, members.getNews());
    }

    @Test
    public void setNews() {
        members.setNews(false);
        assertEquals("The news is wrong", false, members.getNews());
    }

    @Test
    public void getMemberType() {
        assertEquals("The member type is wrong", EnumMem.DEVELOPER, members.getMemberType());
    }

    @Test
    public void setMemberType() {
        members.setMemberType(EnumMem.PRODUCT_OWNER);
        assertEquals("The member type is wrong", EnumMem.PRODUCT_OWNER, members.getMemberType());
    }

    @Test
    public void getAssociations() {
        assertEquals("The associations type is wrong",associations, members.getAssociations());
    }

    @Test
    public void setAssociations() {
        members.setAssociations(associations2);
        assertEquals("The associations type is wrong",associations2, members.getAssociations());
    }

    @Test
    public void getGroups() {
        assertEquals("The groups type is wrong",groups, members.getGroups());
    }

    @Test
    public void setGroups() {
        members.setGroups(groups2);
        assertEquals("The groups type is wrong",groups2, members.getGroups());
    }

    @Test
    public void getComments() {
        assertEquals("The comments type is wrong", comments, members.getComments());
    }

    @Test
    public void setComments() {
        members.setComments(comments2);
        assertEquals("The comments type is wrong",comments2, members.getComments());
    }

    @Test
    public void getMembersTasks() {
        assertEquals("The membersTaskDoings type is wrong", membersTaskDoings, members.getMembersTaskDoings());
    }

    @Test
    public void setMembersTasks() {
        members.setMembersTaskDoings(membersTasksDoing2);
        assertEquals("The membersTaskDoings type is wrong", membersTasksDoing2, members.getMembersTaskDoings());
    }

    @Test
    public void getMemberTaskRunning() {
        Members memberTest = new Members(false,EnumMem.DEVELOPER);
        SprintTasks sprintTasksTest = new SprintTasks("Name", SprintTasks.EnumState.TODO,new BacklogEntries("Name","Description"),50);
        MembersTasksDoing membersTasksDoingTest = new MembersTasksDoing(150,new Date(),sprintTasksTest,memberTest);
        List<MembersTasksDoing> membersTasksDoingListTest = new ArrayList();
        membersTasksDoingListTest.add(membersTasksDoingTest);
        memberTest.setMembersTaskDoings(membersTasksDoingListTest);
    }
 
}