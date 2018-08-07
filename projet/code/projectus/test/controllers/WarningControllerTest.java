package controllers;

import models.Releases;
import models.Sprint;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import play.mvc.Http;

import java.util.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

@RunWith(Enclosed.class)
public class WarningControllerTest {


    abstract public static class Setup {
        static protected InitController initC;
        static protected WarningController warningController;
        static protected Http.Session session;
        static protected Http.RequestBuilder requestBuilder;

        @Before
        public void init() {
            start(ConstantsTest.fakeApp());
            initC = new InitController();
            initC.releasesPerProject = 4;
            initC.idReleaseRunning = 3;
            initC.releaseDuration = 21;
            initC.featuresPerProject = 26;
            initC.sprintsPerRelease = 3;
            initC.targetIndexWarn = 7;
            initC.indexSprintToStart = initC.targetIndexWarn;
            warningController.checkDelay = 0;
        }
        public void setSession() {
            session = ConstantsTest.createSession("2","foobar");
            requestBuilder = ConstantsTest.getRequest("POST", session, routes.ReleasesController.index().url());
            ConstantsTest.setHttpContext(requestBuilder);
        }
        public Sprint getTargetSprint() {
            return Sprint.find.byId(initC.targetIndexWarn+1);
        }
        public Releases getTargetRelease() {
            Releases r = Releases.find.byId(initC.targetIndexWarn+1);
            if (r == null) {
                r = getTargetSprint().getReleases();
            }
            return r;
        }
    }


    public static class WarningTestNoParameterized extends Setup {
        @Test
        public void testEmptyProjects() {
            initC.initEmptyTestProjects();
            setSession();
            assertEquals("should be empty when no release or sprints", "", warningController.warnDeadlinesMessage());
        }
        @Test
        public void testNoWarning() {
            initC.initTest();
            setSession();
            assertEquals("should be empty when not under warning duration from an active sprint or release", "", warningController.warnDeadlinesMessage());
        }
        @Test
        public void testDelayCheck() {
            warningController.checkDelay = 1000;
            initC.initTestCase(1);
            setSession();
            try {
                assertEquals("should check when no cookies", true, warningController.mustReadDates());
                Thread.sleep(100);
                assertEquals("should not check under a delay", false, warningController.mustReadDates());
                Thread.sleep(warningController.checkDelay+100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals("should check after a delay", true, warningController.mustReadDates());
        }
    }


    @RunWith(Parameterized.class)
    public static class WarningTestParameterized  extends Setup {
        private final int sprintPerRelease;
        private final int targetIndex;
        private String caseString;
        private final Boolean warnSprint;
        private final Boolean warnRelease;
        private int testCase;

        public WarningTestParameterized(int testCase, int targetIndex, int sprintPerRelease, Boolean warnSprint, Boolean warnRelease) {
            this.testCase = testCase;
            this.targetIndex = targetIndex;
            this.sprintPerRelease = sprintPerRelease;
            this.warnSprint = warnSprint;
            this.warnRelease = warnRelease;

            this.caseString = "";
            this.caseString += "[case " + String.valueOf(testCase);
            this.caseString += " tIndex " + String.valueOf(targetIndex);
            this.caseString += (warnSprint) ? " warnSprint":" ";
            this.caseString += (warnRelease) ? " warnRelease":" ";
            this.caseString += "] : ";
        }
        @Parameterized.Parameters
        public static Collection testCases() {
            Collection<Object> parameters = new ArrayList<>();
            for (Integer n = 1; n <= warningController.defaultWarningTimes.length; n++) {
                Object[][] params = new Object[][]{
                        {n, 6, 3, true, false},
                        {n, 7, 3, true, false},
                        {n, 8, 3, true, true},
                        {n, 3, 0, false, true},
                };
                parameters.addAll(Arrays.asList(params));
            }
            return parameters;
        }

        @Test
        public void testWarningsSprint() {
            initC.targetIndexWarn = targetIndex;
            initC.indexSprintToStart = targetIndex;
            initC.sprintsPerRelease = sprintPerRelease;
            initC.initTestCase(testCase);
            setSession();

            String message = warningController.warnDeadlinesMessage();
            caseString += "\"" + message + "\" -> ";
            if (warnRelease && !warnSprint) {
                assertTrue(caseString + "should not warn an empty release", message.isEmpty());
            } else {
                assertTrue(caseString + "should be a warning message", !message.isEmpty());
            }
            if (warnSprint) {
                Sprint targetSprint = getTargetSprint();
                assertContains(message, targetSprint.getEndDate(), "sprint");
                if (warnRelease) {
                    Releases targetRelease = getTargetRelease();
                    assertContains(message, targetRelease.getReleaseDate(), "release");
                }

                assertTrue(caseString+"should appear once per session", warningController.warnDeadlinesMessage().isEmpty());
                session.clear();
                setSession();
                assertTrue(caseString+"should reappear at login", ! warningController.warnDeadlinesMessage().isEmpty());
            }
        }

        private void assertContains(String message, Date endDate, String itemName) {
            assertTrue(caseString + "the message should indicate a "+itemName, message.contains(itemName));
            assertTrue(caseString+"should contain a end date for "+itemName,
                    message.contains(warningController.dateFormat.format(endDate)));
        }
    }
}
