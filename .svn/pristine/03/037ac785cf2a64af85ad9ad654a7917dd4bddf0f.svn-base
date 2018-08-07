package models;

import java.util.*;

import factories.*;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static play.test.Helpers.*;


public class SprintTest {
    private final static Date now = new Date();
    private final static Date oneMonthBefore = DateUtils.addMonths(now, -1);
    private final static Date twoWeeksBefore = DateUtils.addWeeks(now, -2);
    private final static Date twoWeeksAfter = DateUtils.addWeeks(now, 2);
    private final static Date oneMonthAfter = DateUtils.addMonths(now, 1);
    private final static Date foutMonthAfter = DateUtils.addMonths(now, 4);
    private final static Releases releasePast = new Releases("past release", twoWeeksBefore, "", false);
    private final static Releases releaseRunning = new Releases("current release", twoWeeksAfter, "", true);
    private final static Releases releaseFuture = new Releases("future release", oneMonthAfter, "", false);

    private Sprint sprint;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() throws Exception {
        sprint = new Sprint("test sprint", twoWeeksBefore, releaseRunning.getReleaseDate(), releaseRunning, "MyDescrip");
        sprint.setState(Sprint.State.RUNNING);
    }

    @Test
    public void isEditableTest() {
        sprint.setState(Sprint.State.INACTIVE);
        assertTrue("a sprint inactive in a running release, with end dates in the future should be editable", sprint.isEditable());
        for (Sprint.State state : Sprint.State.values()) {
            if (state != Sprint.State.INACTIVE) {
                sprint.setState(state);
                assertTrue("a "+ state.toString()+" sprint should not be editable", !sprint.isEditable());
            }
        }

        Sprint sprintPast = new Sprint("aged sprint", oneMonthBefore, releasePast.getReleaseDate(), releasePast, "...");
        assertTrue("a sprint in a past release should not be editable", !sprintPast.isEditable());

        Sprint sprintFuture = new Sprint("baby sprint", twoWeeksAfter, releaseFuture.getReleaseDate(), releaseFuture, "...");
        assertTrue("a inactive sprint in the future should be editable", sprintFuture.isEditable());
        for (Sprint.State state : Sprint.State.values()) {
            if (state != Sprint.State.INACTIVE) {
                sprintFuture.setState(state);
                assertTrue("a "+ state.toString()+" sprint in the future should not be editable", !sprintFuture.isEditable());
            }
        }

        Sprint todaySprint = new Sprint("today sprint", twoWeeksBefore, now, releaseRunning, "...");
        assertTrue("a sprint which end today should be editable", todaySprint.isEditable());

        Sprint yesterdaySprint = new Sprint("today sprint", DateUtils.addHours(now,-20), twoWeeksAfter, releaseRunning, "...");
        assertTrue("A sprint freshly started should be editable", yesterdaySprint.isEditable());

    }

    @Test
    public void percentOfRemainingDay() {
        Sprint sprintTest = new Sprint("Sprint test", oneMonthBefore, oneMonthAfter);
        assertEquals("The percent should be 50%", 50,sprintTest.percentOfRemainingDay(), 1);
        sprintTest.setEndDate(twoWeeksAfter);
        assertEquals("The percent should be 69%", 69,sprintTest.percentOfRemainingDay(), 1);
        sprintTest.setStartDate(twoWeeksBefore);
        sprintTest.setEndDate(oneMonthAfter);
        assertEquals("The percent should be 33%", 33,sprintTest.percentOfRemainingDay(), 1);
    }

    @Test
    public void percentTimeWorked() {
        assertEquals("The percent should be 36%",new Integer(36),sprint.percentTimeWorked(36,100));
    }

    @Test
    public void getNumCompletedTasks() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        sprintTest.setId(25);
        BacklogEntries featureTest = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(5, featureTest);
        int intLoop = 1;
        for (SprintTasks tasksLoop : tasksList) {
            tasksLoop.setId(intLoop);
            if (intLoop <= 2) {
                tasksLoop.setState(SprintTasks.EnumState.DONE);
            }
            intLoop++;
        }
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,featureTest,tasksList);

        assertEquals("2 tasks were completed",new Integer(2),sprintTest.getNumCompletedTasks());
    }


    @Test
    public void getNumInProgressTasks() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        sprintTest.setId(25);
        BacklogEntries featureTest = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(8,featureTest);
        int intLoop = 1;
        for (SprintTasks tasksLoop : tasksList) {
            tasksLoop.setId(intLoop);
            if (intLoop <= 3) {
                tasksLoop.setState(SprintTasks.EnumState.DOING);
            }
            intLoop++;
        }
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,featureTest,tasksList);

        assertEquals("3 task are in progress",new Integer(3),sprintTest.getNumInProgressTasks());
    }

    @Test
    public void getNumNotStartedTasks() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        sprintTest.setId(25);
        BacklogEntries featureTest = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(15, featureTest);
        int intLoop = 1;
        for (SprintTasks tasksLoop : tasksList) {
            tasksLoop.setId(intLoop);
            if (intLoop <= 3) {
                tasksLoop.setState(SprintTasks.EnumState.DOING);
            } else if (intLoop <= 10) {
                tasksLoop.setState(SprintTasks.EnumState.TOREVIEW);
            }
            intLoop++;
        }
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,featureTest,tasksList);

        assertEquals("5 task are not started",new Integer(5),sprintTest.getNumNotStartedTasks());
    }

    @Test
    public void setDates() {
        Date anyDate1 = DateUtils.addMilliseconds(twoWeeksAfter, 1234567);
        Date anyDate2 = DateUtils.addMilliseconds(oneMonthAfter, 456789);
        Sprint sprint = new Sprint("any dates", anyDate1, anyDate2);
        assertTrue("start date should not be any date but the beginning of the day", ! sprint.getStartDate().equals(anyDate1));
        assertTrue("end date should not be any date but the very end of the day", ! sprint.getEndDate().equals(anyDate2));
    }

    @Test
    public void getTotalTimesAndSub() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        BacklogEntries feature = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(20, feature);
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,feature,tasksList);
        sprintTest.setId(12);
        int intLoop = 1;
        for (SprintTasks tasksLoop : tasksList) {
            tasksLoop.setId(intLoop);
            if (intLoop <= 3) {
                tasksLoop.setEstimate(30);
            } else if (intLoop <= 10) {
                tasksLoop.setEstimate(100);
            }
            if (intLoop % 4 == 1) {
                tasksLoop.setState(SprintTasks.EnumState.DOING);
                tasksLoop.setRemainingTime(intLoop * 15);
            } else if (intLoop %4 == 2) {
                tasksLoop.setState(SprintTasks.EnumState.TOREVIEW);
                tasksLoop.setRemainingTimeReview(intLoop * 15);
            }
            intLoop++;
        }


        //TODO : needs to finish factories to create a proper project set in the right way.
        /*List<Sprint> sprintList = new ArrayList<>();
        sprintList.add(sprintTest);
        List<Releases> releasesList = new ArrayList<>();
        Releases releasesTest = new Releases("Name",foutMonthAfter,"Comment",true);
        releasesTest.setSprint(sprintList);
        releasesList.add(releasesTest);
        List<BacklogEntries> featureList = new ArrayList<>();
        featureList.add(feature);
        Groups groupsTest = new Groups("Name","Email");
        Projects projectsTest = new Projects("Description","Project Name",true,releasesList,new ArrayList<Meetings>(), groupsTest, featureList);
        releasesTest.setProjects(projectsTest);
        groupsTest.setProjects(projectsTest);
        releasesTest.setProjects(projectsTest);
        sprintTest.setReleases(releasesTest);

        List<MembersTasksDoing> membersTasksDoingList= new ArrayList<>();
        List<Members> membersList = new ArrayList<>();
        MembersTasksDoing membersTasksDoingTest = new MembersTasksDoing(15, new Date());
        membersTasksDoingList.add(membersTasksDoingTest);
        for (int i = 0; i < 5; i++) {
            try {
                Members members = new Members(new Associations(new Users("Cip","First Name","Last Name"), Associations.EnumAsso.MEMBER),groupsTest, Members.EnumMem.DEVELOPER);
                members.setMemberType(Members.EnumMem.DEVELOPER);
                members.setGroups(groupsTest);
                members.setMembersTaskDoings(membersTasksDoingList);
                membersList.add(members);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        groupsTest.setMembers(membersList);*/

        assertEquals("Total Time Doing",new Integer(1090),sprintTest.getTotalTimeDoing());
        assertEquals("Total Time Review",new Integer(300),sprintTest.getTotalTimeReview());
        assertEquals("Total Time",new Integer(1390),sprintTest.getTotalTime());
        assertEquals("Total Remaining Time", new Integer(2450), sprintTest.getTotalRemainingTime());


        /*assertEquals("Time Worked",new Integer(25),sprintTest.getTimeWorked());
        assertEquals("Remaining Time",new Integer(25),sprintTest.getRemainingTime());
        assertEquals("Remaining Time Estimate",new Integer(25),sprintTest.getRemainingTimeEstimate());*/
    }

    @Test
    public void getRemainingHours() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore, now);
        sprintTest.setExactEndDate(DateUtils.addMinutes(now,65));
        assertEquals("Only 1 hour remaining",1,sprintTest.getRemainingHours());
    }

    @Test
    public void isNotEmpty() {
        Sprint sprintTest = SprintFactory.createNewSprintWithNtasks(0);
        sprintTest.setId(25);
        assertFalse("Sprint empty",sprintTest.isNotEmpty());

        List<SprintTasks> tasksList1 = new ArrayList<>();
        SprintTasks sprintTasksTest1 = new SprintTasks("Name", SprintTasks.EnumState.DONE,sprintTest.getBacklogEntries().get(0),50);
        tasksList1.add(sprintTasksTest1);
        SprintTasksFactory.addAtasksInSprintInNewFeature(sprintTest,tasksList1);

        assertTrue("Sprint with one task",sprintTest.isNotEmpty());
    }

    @Test
    public void getTasks() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        BacklogEntries featureTest = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(50,featureTest);
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,featureTest,tasksList);

        assertEquals(tasksList,sprintTest.getTasks());
    }

    @Test
    public void getTasksDone() {
        Sprint sprintTest = SprintFactory.createNewSprintWithNtasksInFeatures(3,15);

        List<SprintTasks> tasksListDone = new ArrayList<>();
        int intLoop = 0;
        for (SprintTasks tasksLoop : sprintTest.getTasks()) {
            if (intLoop <= 13) {
                tasksListDone.add(tasksLoop);
                tasksLoop.setState(SprintTasks.EnumState.DONE);
            }
            intLoop++;
        }

        assertEquals(tasksListDone,sprintTest.getTasksDone());
    }

    @Test
    public void allTasksDone() {
        Sprint sprintTest = new Sprint("Name", new Date(), new Date());
        BacklogEntries feature = BacklogEntriesFactory.createFeatureWithNnewTasks(15);
        int intLoop = 1;
        for (SprintTasks tasksLoop : feature.getSprintTasks()) {
            tasksLoop.setId(intLoop);
            tasksLoop.setState(SprintTasks.EnumState.DONE);
            intLoop++;
        }
        sprintTest.addBacklogEntrie(feature);


        assertTrue("All tasks are DONE",sprintTest.allTasksDone());

        SprintTasks sprintTasksNotDone = new SprintTasks("Name", SprintTasks.EnumState.TOREVIEW,feature,50);
        sprintTasksNotDone.setId(intLoop);
        feature.getSprintTasks().add(sprintTasksNotDone);
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,feature,feature.getSprintTasks());

        assertFalse("1 task is not finished",sprintTest.allTasksDone());
    }


    @Test
    public void cancelRemainingDoingTasks() {
        start(fakeApplication(inMemoryDatabase()));
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        sprintTest.setId(25);
        BacklogEntries featureTest = new BacklogEntries("Name","Desc");
        List<SprintTasks> tasksList = SprintTasksFactory.createNnewSprintTasksInFeature(50,featureTest);
        SprintTasksFactory.addAtasksInSprintInFeature(sprintTest,featureTest,tasksList);

        int intLoop = 1;
        for (SprintTasks tasksLoop : tasksList) {
            tasksLoop.setId(intLoop);
            switch (intLoop % 4) {
                case 0 :
                    tasksLoop.setState(SprintTasks.EnumState.DOING);
                case 1 :
                    tasksLoop.setState(SprintTasks.EnumState.TOREVIEW);
                case 2 :
                    tasksLoop.setState(SprintTasks.EnumState.DONE);
            }
            intLoop ++ ;
        }

        assertFalse(sprintTest.allTasksDone());
        sprintTest.cancelRemainingDoingTasks();
        for (SprintTasks tasksLoop : tasksList) {
            if (tasksLoop.getState() != SprintTasks.EnumState.DONE) {
                assertTrue(tasksLoop.getState() == SprintTasks.EnumState.TODO);
            }
        }
    }

    @Test
    public void getActivatedBacklogEntries() {
        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        List<BacklogEntries> activatedFeatures = new ArrayList<>();
        for (int loop=1; loop<11; loop++){
            BacklogEntries featureTest = new BacklogEntries("Name","Desc");
            featureTest.setSprint(sprint);
            sprintTest.addBacklogEntrie(featureTest);
            featureTest.setActivate(false);
            if ( (loop % 3) == 0 ) {
                featureTest.setActivate(true);
                activatedFeatures.add(featureTest);
            }
        }

        assertEquals("Only 3 features are activated",activatedFeatures,sprintTest.getActivatedBacklogEntries());
    }

    @Test
    public void getBacklogEntriesByNumber() {

        Sprint sprintTest = new Sprint("Name",twoWeeksBefore,twoWeeksAfter);
        ArrayList<BacklogEntries> featuresOrdered = new ArrayList<>();
        for (int loop=1; loop<11; loop++){
            BacklogEntries featureTest = new BacklogEntries("Name","Desc");
            featureTest.setSprint(sprint);
            sprintTest.addBacklogEntrie(featureTest);
            featureTest.setId(loop);
            featuresOrdered.add(featureTest);
        }

        assertEquals("Only 3 features are activated",featuresOrdered,sprintTest.getActivatedBacklogEntries());
    }
}