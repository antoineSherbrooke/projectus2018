package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import utils.Dates;

import static org.junit.Assert.*;


public class ReleasesTest {

  private Releases releases, releasesNameDateComRunProject;
  private Projects projectTest;
  private final static Date now = new Date();
  private final static Date before = DateUtils.addMonths(now, -1);
  private final static Date after = DateUtils.addMonths(now, 1);
  private final static Date after2 = DateUtils.addMonths(after, 1);


  @Before
  public void before() throws Exception {
    projectTest = new Projects("Desc","Name",true);
    releases = new Releases("MyName",now,"MyComment",true);
    releasesNameDateComRunProject = new Releases("ReleasesNameDateComRunProject",now,"Comment",true,projectTest);
  }

  @After
  public void after() throws Exception {
    releases = null;
    releasesNameDateComRunProject = null;
    projectTest = null;
  }
  
  @Test
  public void releases() {
    assertNotNull("The instance is not created", releases);
    assertNotNull("The instance is not created", releasesNameDateComRunProject);
  }

  @Test
  public void getName() {
    assertEquals("The name is wrong", "MyName", releases.getName());
  }
  
  @Test
  public void setName() {
    releases.setName("MyName2");
    assertEquals("The name is wrong", "MyName2", releases.getName());
  }

  @Test
  public void getComment() {
    assertEquals("The comment is wrong", "MyComment", releases.getComment());
  }

  @Test
  public void setComment() {
    releases.setComment("MyComment2");
    assertEquals("The comment is wrong", "MyComment2", releases.getComment());
  }

  @Test
  public void getReleaseDate() {
    assertEquals("The release date is wrong", Dates.getEndOfDay(now), releases.getReleaseDate());
  }

  @Test
  public void setReleaseDate() {
    releases.setReleaseDate(after);
    assertEquals("The release date is wrong", Dates.getEndOfDay(after), releases.getReleaseDate());
  }

  @Test
  public void setExactReleaseDate() {
    releasesNameDateComRunProject.setExactReleaseDate(now);
    assertEquals("The release date is wrong", Dates.getEndOfDay(now), releases.getReleaseDate());
  }

  @Test
  public void getId() {
    assertEquals("Wrong ID",0,releasesNameDateComRunProject.getId());
  }

  @Test
  public void setId() {
    releasesNameDateComRunProject.setId(33);
    assertEquals("Wrong ID",33,releasesNameDateComRunProject.getId());
  }

  @Test
  public void getRunning() {
    assertEquals("Release running",true,releasesNameDateComRunProject.getRunning());
  }

  @Test
  public void setRunning() {
    releasesNameDateComRunProject.setRunning(false);
    assertEquals("Release running",false,releasesNameDateComRunProject.getRunning());
  }

  @Test
  public void getProject() {
    assertEquals("Wrong project",projectTest,releasesNameDateComRunProject.getProjects());
  }

  @Test
  public void setProject() {
    Projects projectTest2 = new Projects("Description","Name of the 2nd project",true);
    releasesNameDateComRunProject.setProjects(projectTest2);
    assertEquals("Wrong project set",projectTest2,releasesNameDateComRunProject.getProjects());
  }

  @Test
  public void getActive() {
    assertEquals("The release is active",true,releasesNameDateComRunProject.getActive());
  }

  @Test
  public void setActive() {
    releasesNameDateComRunProject.setActive(false);
    assertEquals("The release is not active",false,releasesNameDateComRunProject.getActive());
  }

  @Test
  public void getSprintSorted() {
    List<Sprint> listSprintsTest = new ArrayList<Sprint>();
    List<Sprint> listSprintsSorted = new ArrayList<Sprint>();
    Sprint sprintTest1 = new Sprint();
    sprintTest1.setState(Sprint.State.INACTIVE);
    sprintTest1.setStartDate(now);
    sprintTest1.setEndDate(after);
    Sprint sprintTest2 = new Sprint();
    sprintTest2.setState(Sprint.State.RUNNING);
    sprintTest2.setStartDate(after);
    sprintTest2.setEndDate(after2);
    listSprintsTest.add(sprintTest2);
    listSprintsTest.add(sprintTest1);
    listSprintsSorted.add(sprintTest1);
    listSprintsSorted.add(sprintTest2);
    releases.setSprint(listSprintsTest);
    assertNotEquals("List non equal",releases.getSprint(),listSprintsSorted);
    assertEquals("List non equal",releases.getSprintSorted(),listSprintsSorted);
  }

  @Test
  public void hasActiveSprint() {
    List<Sprint> listSprintsTest = new ArrayList<>();
    Sprint sprintTest1 = new Sprint();
    sprintTest1.setState(Sprint.State.RUNNING);
    listSprintsTest.add(sprintTest1);
    releases.setSprint(listSprintsTest);
    assertTrue("The release test has active sprint",releases.hasActiveSprint());
  }

  @Test
  public void checkSprintRunning() {
    List<Sprint> listSprintsTest = new ArrayList<Sprint>();
    Sprint sprintTest1 = new Sprint();
    sprintTest1.setState(Sprint.State.RUNNING);
    Sprint sprintTest2 = new Sprint();
    sprintTest2.setState(Sprint.State.RUNNING);
    listSprintsTest.add(sprintTest1);
    listSprintsTest.add(sprintTest2);
    releases.setSprint(listSprintsTest);
    assertTrue("The test sprint is running",releases.checkSprintRunning());
  }

  @Test
  public void isPast() {
    releases.setReleaseDate(before);
    assertTrue("The release date is older than today",releases.isPast());
    releases.setReleaseDate(after);
    assertFalse("The release date is after today",releases.isPast());
    releases.setReleaseDate(now);
    assertFalse("The release date is today",releases.isPast());
  }

  @Test
  public void dateFormat() {
    assertEquals("Wrong date format",releases.dateFormat(new Date(116,05,28)),"Tuesday, 28 Jun, 2016");
    assertEquals("Wrong date format",releases.dateFormat(null),"");
  }

  @Test
  public void checkRunning() {
    List<Releases> listRealeseTest = new ArrayList<>();
    releases.setRunning(true);
    releasesNameDateComRunProject.setRunning(true);
    listRealeseTest.add(releases);
    listRealeseTest.add(releasesNameDateComRunProject);
    assertEquals("Wrong running release",releases,Releases.checkRunning(listRealeseTest));
  }
}