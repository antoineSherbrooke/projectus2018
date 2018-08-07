package controllers;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.MeetingMembers;
import models.Meetings;
import models.Projects;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;


public class MeetingsControllerTest extends WithApplication{

    private Projects project = new Projects("MyDescription","MyName",true);
    private MeetingsController meetingsController = new MeetingsController();
    private Meetings meeting = new Meetings("MyName",true,project);
    private MeetingMembers meetingMember = new MeetingMembers("aeri9003",true,meeting);
    private MeetingMembers meetingMember2 = new MeetingMembers("aeri9004",false,meeting);
    private List<Meetings> meetingsListTrue = new ArrayList<>();
    private List<Meetings> meetingsListFalse = new ArrayList<>();
    private List<Meetings> meetingsListTrueAndFalse = new ArrayList<>();
    private Meetings meeting2 = new Meetings("MyName2",true,project);
    private Meetings meeting3 = new Meetings("MyName3",false,project);
    private Meetings meeting4 = new Meetings("MyName4",false,project);
    private Meetings meeting5 = new Meetings("MyName5",true,project);
    private Http.Session session;
    private Http.RequestBuilder request;
    private Map<String,String> map = new HashMap<>();

    @Before
    public void before() throws Exception {
        start(ConstantsTest.fakeApp());
        new InitController().initTest();
        session = ConstantsTest.createSession("2","foobar");

        project.save();
        meetingsListTrue.add(meeting);
        meetingsListTrue.add(meeting2);
        meetingsListTrue.add(meeting5);
        meetingsListTrueAndFalse.add(meeting);
        meetingsListTrueAndFalse.add(meeting2);
        meetingsListTrueAndFalse.add(meeting3);
        meetingsListTrueAndFalse.add(meeting4);
        meetingsListTrueAndFalse.add(meeting5);
        meetingsListFalse.add(meeting3);
        meetingsListFalse.add(meeting4);
        meeting.save();
        meeting2.save();
        meeting3.save();
        meeting4.save();
        meeting5.save();
        map.put("name","MyName");
        map.put("dayOrder","MyDayOrder");
//        map.put("members","(aeri9003,true)(aeri9004,false)");
        ArrayNode members = Json.newArray();
        ObjectNode o1 = Json.newObject().put("cip", "aeri9003").put("checked", true);
        ObjectNode o2 = Json.newObject().put("cip", "aeri9004").put("checked", false);
        members.add(o1);
        members.add(o2);
        map.put("members", members.toString());
        map.put("conclusion","MyConclusion");
        map.put("timeWorked", "270");
    }

    @Test
    public void addMeeting(){
        request = ConstantsTest.getRequest("POST",session,routes.MeetingsController.addMeeting().url());
        request.bodyForm(map);
        Result result = route(request);
        assertEquals("This is not the page add meetings","/projectus/meetings/add", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void infoMeeting(){
        request = ConstantsTest.getRequest("POST",session,routes.MeetingsController.infoMeeting(1).url());
        Result result = route(request);
        assertEquals("This is not the page conclude meetings","/projectus/meetings/info/1", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void index(){
        request = ConstantsTest.getRequest("GET",session,routes.MeetingsController.index().url());
        Result result = route(request);
        assertEquals("This is not the page meetings","/projectus/meetings", request.uri());
        assertEquals("The page should be work", OK, result.status());
    }

    @Test
    public void ifNoMeetingStarted(){
        assertTrue("The function ifNoMeetingStarted doesn't work -> all true",meetingsController.ifNoMeetingStarted(meetingsListTrue));
        assertFalse("The function ifNoMeetingStarted doesn't work -> list true and false",meetingsController.ifNoMeetingStarted(meetingsListTrueAndFalse));
        assertFalse("The function ifNoMeetingStarted doesn't work -> all false",meetingsController.ifNoMeetingStarted(meetingsListFalse));
    }

    @Test
    public void ifNoMeetingCompleted(){
        assertFalse("The function ifNoMeetingCompleted doesn't work -> all true",meetingsController.ifNoMeetingCompleted(meetingsListTrue));
        assertFalse("The function ifNoMeetingCompleted doesn't work -> list true and false",meetingsController.ifNoMeetingCompleted(meetingsListTrueAndFalse));
        assertTrue("The function ifNoMeetingCompleted doesn't work -> all false",meetingsController.ifNoMeetingCompleted(meetingsListFalse));
    }



}
