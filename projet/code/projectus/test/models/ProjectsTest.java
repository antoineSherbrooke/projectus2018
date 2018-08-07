package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ProjectsTest {

    private Date date = new Date(120);
    private Groups groups = new Groups("MyName","MyEmail");
    private Groups groups2 = new Groups("MyName2","MyEmail2");
    private Meetings meet = new Meetings("MySubject","MyConclusion");
    private Meetings meet2 = new Meetings("MySubject","MyConclusion");
    private List<Meetings> meetings = new ArrayList();
    private List<Meetings> meetings2 = new ArrayList();
    private Releases rel = new Releases("MyName",date,"MyComment",true);
    private Releases rel2 = new Releases("MyName2",date,"MyComment2",true);
    private List<Releases> releases = new ArrayList();
    private List<Releases> releases2 = new ArrayList();

    private Projects projects;

    @Before
    public void before() throws Exception {
        releases.add(rel);
        releases2.add(rel2);
        meetings.add(meet);
        meetings2.add(meet2);
        projects = new Projects("MyDescription","MyName",true,releases,meetings,groups);
    }

    @After
    public void after() throws Exception {
        projects = null;
    }

    @Test
    public void sprinttasks() {
        assertNotNull("The instance is not created", projects);
    }

    @Test
    public void getName() {
        assertEquals("The name is wrong", "MyName", projects.getName());
    }

    @Test
    public void setName() {
        projects.setName("MyName2");
        assertEquals("The name is wrong", "MyName2", projects.getName());
    }
    @Test
    public void getDescription() {
        assertEquals("The description is wrong", "MyDescription", projects.getDescription());
    }

    @Test
    public void setDescription() {
        projects.setDescription("MyDescription2");
        assertEquals("The description is wrong", "MyDescription2", projects.getDescription());
    }
    @Test
    public void getState() {
        assertEquals("The state is wrong",true, projects.getState());
    }

    @Test
    public void setState() {
        projects.setState(false);
        assertEquals("The state is wrong", false, projects.getState());
    }
    @Test
    public void getReleases() {
        assertEquals("The releases is wrong",releases, projects.getReleases());
    }

    @Test
    public void setReleases() {
        projects.setReleases(releases2);
        assertEquals("The releases is wrong", releases2, projects.getReleases());
    }
    @Test
    public void getMeetings() {
        assertEquals("The meetings is wrong",meetings, projects.getMeetings());
    }

    @Test
    public void setMeetings() {
        projects.setMeetings(meetings2);
        assertEquals("The meetings is wrong", meetings2, projects.getMeetings());
    }
    @Test
    public void getGroups() {
        assertEquals("The groups is wrong",groups, projects.getGroups());
    }

    @Test
    public void setGroups() {
        projects.setGroups(groups2);
        assertEquals("The groups is wrong", groups2, projects.getGroups());
    }

    @Test
    public void getListSupervisorsToString() {
        Projects projectsTest = new Projects("Description","Name",true);
        List<Projects> projectsList = new ArrayList<>();
        projectsList.add(projectsTest);
        Users userSupervisor = new Users("MyCIP","Moris","Dupuis","MyPassword");
        Users userSupervisor2 = new Users("MyCIP","Claire","Chazale","MyPassword");
        Members memberSupervisor = new Members(true, Members.EnumMem.DEVELOPER);
        Associations associationSupervisor = new Associations(userSupervisor, Associations.EnumAsso.SUPERVISOR, memberSupervisor);
        Associations associationSupervisor2 = new Associations(userSupervisor2, Associations.EnumAsso.SUPERVISOR, memberSupervisor);
        Supervisors supervisorsTest = new Supervisors(associationSupervisor,projectsList);
        Supervisors supervisorsTest2 = new Supervisors(associationSupervisor2,projectsList);
        List<Supervisors> supervisorsList = new ArrayList<>();
        supervisorsList.add(supervisorsTest);
        supervisorsList.add(supervisorsTest2);
        projectsTest.setSupervisors(supervisorsList);

        String supervisorStringTarget = "";
        for (Supervisors supervisorsLoop : supervisorsList) {
            supervisorStringTarget = supervisorStringTarget + ", " + supervisorsLoop.getAssociations().getUsers().getFirstName() + " " + supervisorsLoop.getAssociations().getUsers().getLastName();
        }
        supervisorStringTarget = supervisorStringTarget.substring(2) + ".";

        assertEquals(supervisorStringTarget,projectsTest.getListSupervisorsToString());
    }


}