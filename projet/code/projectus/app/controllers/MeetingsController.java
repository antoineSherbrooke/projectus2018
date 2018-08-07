package controllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Security.Secure;
import com.avaje.ebean.Ebean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.meeting;

import javax.inject.Inject;


@With(Secure.class)
public class MeetingsController extends Controller {

    @Inject FormFactory formFactory;

    public Result index(){
        return ok(meeting.render(getActualMeetings(), getActualMeetingMembers()));
    }

    private List<Meetings> getActualMeetings(){
        Projects runningProject = SessionController.getProjectRunning();
        List<Meetings> meetings = new ArrayList<>();
        try {
            meetings = Sprint.sprintRunning(runningProject).getMeeting();
        }catch (Exception e){}
        return meetings;
    }

    private  List<MeetingMembers> getActualMeetingMembers(){
        Projects runningProject = SessionController.getProjectRunning();
        List<Members> membersInProject = runningProject.getGroups().getMembers();
        ArrayList<MeetingMembers> meetingMembers = new ArrayList<>();

        membersInProject.stream().filter(member -> member.getActive()).forEach(member -> {
            MeetingMembers meetingMember = new MeetingMembers(member.getUser().getCip(), false);
            Boolean alreadyThere = meetingMembers.stream().anyMatch(
                    mm -> mm.getMemberCIP().equals(meetingMember.getMemberCIP()));
            Boolean isAdministrator = member.getMemberType().equals(Members.EnumMem.ADMINISTRATOR);
            if (!alreadyThere && !isAdministrator) {
                meetingMembers.add(meetingMember);
            }
        });
        return meetingMembers;
    }

    public Result addMeeting(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String name = requestData.get("name");
        String dayOrder = requestData.get("dayOrder");
        JsonNode members = Json.parse(requestData.get("members"));
        Integer timeWorked = Integer.valueOf(requestData.get("timeWorked"));
        String conclusion = requestData.get("conclusion");

        ObjectNode result = Json.newObject();
        Projects runningProject = SessionController.getProjectRunning();

        if(name.isEmpty() || dayOrder.isEmpty() || conclusion.isEmpty()){
            return badRequest("Fields are required");
        }

        Meetings meeting = new Meetings(name,dayOrder, 0, true, runningProject,
                Sprint.sprintRunning(runningProject), conclusion,
                timeWorked, new Date());
        meeting.save();

        List<MeetingMembers> meetingMembersList = createMeetingMembers(members,meeting);
        for(MeetingMembers m : meetingMembersList){
            m.save();
        }
        result.put("id", meeting.getId());
        return ok(result);

    }

    public Result concludeMeeting() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Integer id = Integer.valueOf(requestData.get("id"));
        JsonNode members = Json.parse(requestData.get("members"));
        Integer timeWorked = Integer.valueOf(requestData.get("timeWorked"));
        String conclusion = requestData.get("conclusion");

        Meetings meeting = Meetings.find.byId(id);
        meeting.setTimeWorked(timeWorked);
        meeting.setConclusion(conclusion);
        meeting.setActivate(true);
        meeting.update();

        List<MeetingMembers> meetingMembersList = createMeetingMembers(members, meeting);
        Ebean.saveAll(meetingMembersList);

        ObjectNode result = Json.newObject();
        result.put("id", meeting.getId());
        return ok(result);

    }

    public Result infoMeeting(Integer id){
        ObjectNode result = Json.newObject();
        Meetings meeting = Meetings.find.byId(id);

        ArrayNode membersArrayNode = Json.newArray();
        for(MeetingMembers member : meeting.getMeetingMembers()) {
            ObjectNode mem = Json.newObject();
            mem.put("active", member.isActive());
            mem.put("name", member.getName());
            mem.put("cip", member.getMemberCIP());
            membersArrayNode.add(mem);
        }
        result.put("id",meeting.getId());
        result.put("name",meeting.getName());
        result.put("dayorder",meeting.getDayOrder());
        result.put("conclusion",meeting.getConclusion());
        result.put("timeworked",meeting.getTimeWorked());
        result.put("activate",meeting.isActivate());
        result.put("firstestimate",meeting.getFirstEstimate());
        result.put("meetingMembers",membersArrayNode);

        return ok(result);
    }

    private List<MeetingMembers> createMeetingMembers(JsonNode members, Meetings meeting){
        List<MeetingMembers> meetingMembers= new ArrayList<>();
        for (JsonNode member : members) {
            String cip = member.get("cip").toString().replaceAll("\"", "");
            Boolean checked = member.get("checked").asBoolean();
            meetingMembers.add(new MeetingMembers(cip, checked, meeting));
        }

        return meetingMembers;
    }

    public static Boolean ifNoMeetingStarted(List<Meetings> meetings){
        return meetings.stream().allMatch(Meetings::isActivate);
    }
    public static Boolean ifNoMeetingCompleted(List<Meetings> meetings){
        return meetings.stream().noneMatch(Meetings::isActivate);
    }
}

