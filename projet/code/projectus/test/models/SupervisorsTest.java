package models;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SupervisorsTest {

    private Groups groups = new Groups("MyName","MyEmail");
    private List<Meetings> meetings = new ArrayList();
    private List<Releases> releases = new ArrayList();
    private Projects project = new Projects("MyDescription","MyName",true,releases,meetings,groups);
    private Projects project2 = new Projects("MyDescription","MyName",true,releases,meetings,groups);
    private Projects project3 = new Projects("MyDescription","MyName",true,releases,meetings,groups);
    private List<Projects> projectList = new ArrayList();
    private List<Projects> projectList2 = new ArrayList();
    private Members member = new Members(true, Members.EnumMem.DEVELOPER);
    private Users user = new Users("MyCIP","MyFirstName","MyLastName","MyPassword");
    private Users user2 = new Users("MyCIP2","MyFirstName2","MyLastName2","MyPassword2");
    private Associations association = new Associations(user, Associations.EnumAsso.MEMBER, member);
    private Associations association2 = new Associations(user2, Associations.EnumAsso.MEMBER, member);
    private Supervisors supervisors;


    @Before
    public void before(){
        projectList.add(project);
        projectList.add(project2);
        projectList2.add(project2);
        projectList2.add(project3);
        supervisors = new Supervisors(association,projectList);
    }

    @Test
    public void getAssociations(){
        assertEquals("The association is wrong",association, supervisors.getAssociations());
    }
    @Test
    public void setAssociations(){
        supervisors.setAssociations(association2);
        assertEquals("The association is wrong",association2, supervisors.getAssociations());
    }
    @Test
    public void getProjects(){
        assertEquals("The project is wrong",projectList, supervisors.getProjects());
    }
    @Test
    public void setProjects(){
        supervisors.setProjects(projectList2);
        assertEquals("The project is wrong",projectList2, supervisors.getProjects());
    }



}
