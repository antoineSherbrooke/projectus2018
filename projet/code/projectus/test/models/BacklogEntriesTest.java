package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;

import models.SprintTasks.EnumState;

public class BacklogEntriesTest {
    private BacklogEntries backlogentries;
    private static Date startDate = new Date(120);
    private static Date endDate = new Date(122);
    private static Sprint sprint;
    private static SprintTasks task;
    private static SprintTasks task2;

    private List<SprintTasks> sprinttasks = new ArrayList<>();
    private List<SprintTasks> sprinttasks2 = new ArrayList<>();

    @BeforeClass
    public static void once() {
        sprint = new Sprint("This sprint Name", startDate, endDate);
        sprint.setState(Sprint.State.RUNNING);
        task = new SprintTasks("MyName", EnumState.TODO, "MyDescription", 8);
        task2 = new SprintTasks("MyName2", EnumState.TODO, "MyDescription2", 8);
    }

    @Before
    public void before() throws Exception {
        sprinttasks.add(task);
        sprinttasks2.add(task2);
        backlogentries = new BacklogEntries("MyName","MyDescription",1,5, BacklogEntries.EnumFirstEstimate.LONG,15,BacklogEntries.EnumPriority.VERYHIGH, 25, sprinttasks, sprint);
    }

    @After
    public void after() throws Exception {
        backlogentries = null;
    }

    @Test
    public void sprinttasks() {
        assertNotNull("The instance is not created", backlogentries);
    }

    @Test
    public void getName() {
        assertEquals("The name is wrong","MyName", backlogentries.getName());
    }

    @Test
    public void setName() {
        backlogentries.setName("MyName2");
        assertEquals("The name min is wrong","MyName2", backlogentries.getName());
    }
    @Test
    public void getDescription() {
        assertEquals("The description is wrong","MyDescription", backlogentries.getDescription());
    }

    @Test
    public void setDescription() {
        backlogentries.setDescription("MyDescription2");
        assertEquals("The description min is wrong","MyDescription2", backlogentries.getDescription());
    }
    @Test
    public void getState() {
        assertEquals("The state is wrong",1, backlogentries.getState());
    }

    @Test
    public void setState() {
        backlogentries.setState(2);
        assertEquals("The state min is wrong",2, backlogentries.getState());
    }
    @Test
    public void getNumber() {
        assertEquals("The number is wrong",5, backlogentries.getNumber());
    }

    @Test
    public void setNumber() {
        backlogentries.setNumber(6);
        assertEquals("The number min is wrong",6, backlogentries.getNumber());
    }
    @Test
    public void getFirstEstimate() {
        assertEquals("The first estimate is wrong",BacklogEntries.EnumFirstEstimate.LONG, backlogentries.getFirstEstimate());
    }

    @Test
    public void setFirstEstimate() {
        backlogentries.setFirstEstimate(BacklogEntries.EnumFirstEstimate.VERYLONG);
        assertEquals("The first  estimate  is wrong",BacklogEntries.EnumFirstEstimate.VERYLONG, backlogentries.getFirstEstimate());
    }
    @Test
    public void getParentId() {
        assertEquals("The parent id is wrong",15, backlogentries.getParentId());
    }

    @Test
    public void setParentId() {
        backlogentries.setParentId(16);
        assertEquals("The parent id is wrong",16, backlogentries.getParentId());
    }
    @Test
    public void getPriority() {
        assertEquals("The priority is wrong", BacklogEntries.EnumPriority.VERYHIGH, backlogentries.getPriority());
    }

    @Test
    public void setPriority() {
        backlogentries.setPriority(BacklogEntries.EnumPriority.AVERAGE);
        assertEquals("The priority is wrong", BacklogEntries.EnumPriority.AVERAGE, backlogentries.getPriority());
    }
    @Test
    public void getTotalTime() {
        assertEquals("The total time is wrong",25, backlogentries.getTotalTime());
    }

    @Test
    public void setTotalTime() {
        backlogentries.setTotalTime(26);
        assertEquals("The total time is wrong",26, backlogentries.getTotalTime());
    }

    @Test
    public void getSprintTasks() {
        assertEquals("The sprintTasks is wrong",sprinttasks, backlogentries.getSprintTasks());
    }

    @Test
    public void setSprintTasks() {
        backlogentries.setSprintTasks(sprinttasks2);
        assertEquals("The sprintTasks is wrong",sprinttasks2, backlogentries.getSprintTasks());
    }

}