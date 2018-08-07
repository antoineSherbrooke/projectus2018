package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import factories.SprintTasksFactory;
import models.SprintTasks.EnumState;


import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;


public class SprintTasksTest {

    private SprintTasks sprinttasks;
    private final static Date now = new Date();
    private final static Date oneMonthBefore = DateUtils.addMonths(now, -1);
    private final static Date twoWeeksBefore = DateUtils.addWeeks(now, -2);
    private final static Date twoWeeksAfter = DateUtils.addWeeks(now, 2);
    private final static Date oneMonthAfter = DateUtils.addMonths(now, 1);
    private Comments co = new Comments("MyTitle","MyContent",now);
    private Comments co2 = new Comments("MyTitle2","MyContent2",twoWeeksAfter);
    private MembersTasksDoing ms = new MembersTasksDoing(50,twoWeeksAfter);
    private MembersTasksDoing ms2 = new MembersTasksDoing(100,twoWeeksAfter);
    private BacklogEntries backlogentries = new BacklogEntries("MyName","MyDescription",1,5, BacklogEntries.EnumFirstEstimate.LONG,15, BacklogEntries.EnumPriority.HIGH,25);
    private Sprint sprintTest = new Sprint("Sprint Name",twoWeeksBefore,oneMonthAfter);
    private List<Comments> comments = new ArrayList();
    private List<Comments> comments2 = new ArrayList();
    private List<MembersTasksDoing> membersTaskDoings = new ArrayList();
    private List<MembersTasksDoing> membersTasksDoing2 = new ArrayList();


    @Before
    public void before() throws Exception {
        membersTaskDoings.add(ms);
        membersTasksDoing2.add(ms);
        membersTasksDoing2.add(ms2);
        comments.add(co);
        comments2.add(co2);
        sprinttasks = new SprintTasks("MyName",EnumState.TODO,backlogentries,8,"MyDescription",comments, membersTaskDoings);
        sprinttasks.setSprint(sprintTest);
        sprinttasks.setId(1);
        sprinttasks.setStartDate(twoWeeksBefore);
        sprinttasks.setEndDate(oneMonthAfter);
    }

    @After
    public void after() throws Exception {
        sprinttasks = null;
    }

    @Test
    public void sprintTasks() {
        assertNotNull("The instance is not created", sprinttasks);
    }

    @Test
    public void getName() {
        assertEquals("The name is wrong", "MyName", sprinttasks.getName());
    }

    @Test
    public void setName() {
        sprinttasks.setName("MyName2");
        assertEquals("The name is wrong", "MyName2", sprinttasks.getName());
    }
    @Test
    public void getDescription() {
        assertEquals("The description is wrong", "MyDescription", sprinttasks.getDescription());
    }

    @Test
    public void setDescription() {
        sprinttasks.setDescription("MyDescription2");
        assertEquals("The description is wrong", "MyDescription2", sprinttasks.getDescription());
    }
    @Test
    public void getEstimate() {
        assertEquals("The first estimate is wrong",8, sprinttasks.getEstimate());
    }

    @Test
    public void setEstimate() {
        sprinttasks.setEstimate(9);
        assertEquals("The first estimate is wrong", 9, sprinttasks.getEstimate());
        assertEquals("The remaining time should be 9", 9, sprinttasks.getRemainingTime());
    }

    @Test
    public void getState() {
        assertEquals("The state is wrong",EnumState.TODO, sprinttasks.getState());
    }

    @Test
    public void setState() {
        sprinttasks.setState(EnumState.DONE);
        assertEquals("The state is wrong",EnumState.DONE, sprinttasks.getState());
    }
    @Test
    public void getComments() {
        assertEquals("The comments is wrong",comments, sprinttasks.getComments());
    }

    @Test
    public void setComments() {
        sprinttasks.setComments(comments2);
        assertEquals("The comments is wrong",comments2, sprinttasks.getComments());
    }
    @Test
    public void getMembersTasks() {
        assertEquals("The membersTaskDoings is wrong", membersTaskDoings, sprinttasks.getMembersTaskDoings());
    }

    @Test
    public void setMembersTasks() {
        sprinttasks.setMembersTaskDoings(membersTasksDoing2);
        assertEquals("The membersTaskDoings is wrong", membersTasksDoing2, sprinttasks.getMembersTaskDoings());
    }

    @Test
    public void getSprint() {
        assertEquals("The sprint should be sprintTest",sprintTest,sprinttasks.getSprint());
    }

    @Test
    public void setSprint() {
        Sprint sprintTest2 = new Sprint("Name",oneMonthBefore,twoWeeksAfter);
        sprinttasks.setSprint(sprintTest2);
        assertEquals("The sprint should be sprintTest",sprintTest2,sprinttasks.getSprint());
    }

    @Test
    public void getId() {
        assertEquals("The id should be 1",new Integer(1),sprinttasks.getId());
    }

    @Test
    public void setId() {
        sprinttasks.setId(15);
        assertEquals("The id should be 15",new Integer(15),sprinttasks.getId());
    }

    @Test
    public void getBacklogEntries() {
        assertEquals("Wrong backlog entries",backlogentries,sprinttasks.getBacklogEntries());
    }

    @Test
    public void setBacklogEntries() {
        BacklogEntries backlogEntriesTest = new BacklogEntries("Name","Descrption");
        sprinttasks.setBacklogEntries(backlogEntriesTest);
        assertEquals("Wrong backlog entry",backlogEntriesTest,sprinttasks.getBacklogEntries());
    }

    @Test
    public void setEstimateReview() {
        sprinttasks.setEstimateReview(65);
        assertEquals("The first estimate is wrong", 65, sprinttasks.getEstimateReview());
        assertEquals("The remaining time should be 65", 65, sprinttasks.getRemainingTimeReview());
    }

    @Test
    public void getStartDate() {
        assertEquals("The start date is wrong",twoWeeksBefore,sprinttasks.getStartDate());
    }

    @Test
    public void setStartDate() {
        sprinttasks.setStartDate(twoWeeksAfter);
        assertEquals("The start date is wrong",twoWeeksAfter,sprinttasks.getStartDate());
    }

    @Test
    public void getEndDate() {
        assertEquals("The start date is wrong",oneMonthAfter,sprinttasks.getEndDate());
    }

    @Test
    public void setEndDate() {
        sprinttasks.setEndDate(twoWeeksAfter);
        assertEquals("The start date is wrong",twoWeeksAfter,sprinttasks.getEndDate());
    }

    @Test
    public void getWorkingTime() {
        SprintTasks sprintTasksTest = SprintTasksFactory.createNewEmptyTasks();
        sprintTasksTest.setMembersTaskDoings(membersTasksDoing2);
        List<MembersTasksDoing> membersListTest = sprintTasksTest.getMembersTaskDoings();
        int i = 50;
        for ( MembersTasksDoing mLT : membersListTest) {
            mLT.setTimeSpent(i);
            i =+ 50;
        }
        assertEquals("The time spent should be 100",new Integer(100),sprintTasksTest.getWorkingTime());
    }

    @Test
    public void getWorkingTimeReview() {
        SprintTasks sprintTasksTest = SprintTasksFactory.createNewEmptyTasks();
        List<MembersTasksReview> membersTasksReview = new ArrayList<>();
        membersTasksReview.add(new MembersTasksReview(50,now));
        membersTasksReview.add(new MembersTasksReview(100,now));
        sprintTasksTest.setMembersTasksReviews(membersTasksReview);
        assertEquals("The time spent should be 150",new Integer(150),sprintTasksTest.getWorkingTimeReview());
    }

    @Test
    public void getProgress() {
        SprintTasks sprintTasksTest = SprintTasksFactory.createNewEmptyTasks();
        sprintTasksTest.setMembersTaskDoings(membersTasksDoing2);
        sprintTasksTest.setRemainingTime(200);
        assertEquals("The remaining time should be 75", 75, sprintTasksTest.getProgress());
    }

    @Test
    public void getProgressReview() {
        SprintTasks sprintTasksTest = SprintTasksFactory.createNewEmptyTasks();
        List<MembersTasksReview> membersTasksReview = new ArrayList<>();
        membersTasksReview.add(new MembersTasksReview(300,now));
        membersTasksReview.add(new MembersTasksReview(100,now));
        sprintTasksTest.setMembersTasksReviews(membersTasksReview);
        sprintTasksTest.setRemainingTimeReview(1000);
        assertEquals("The remaining time should be 40", 40, sprintTasksTest.getProgressReview());
    }

}