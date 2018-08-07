package controllers;

import Security.Secure;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.MembersTasks;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Dates;

import java.util.*;

@With(Secure.class)
public class ChartsController extends Controller {

    public Result getDatasMembers(Integer sprintId){
        ObjectNode result = Json.newObject();
        Projects projects= SessionController.getProjectRunning();
		Groups group = projects.getGroups();
        Sprint sprint = Sprint.find.byId(sprintId);
        if (sprint != null) {
            List<Members> members = group.getMembers();
            result.put("times", getTimesMembers(members, sprint));
            result.put("numOfDays", Dates.dayDiff(sprint.getStartDate(), sprint.getEndDate()));
            result.put("numOfTasks", sprint.getTasks().size());
            result.put("burnDown", getDatasBurnDown(sprint));
            return ok(result);
        } else {
            return badRequest("sprint with id "+sprintId+" doesnt exist");
        }
	}

    public ArrayNode getTimesMembers(List<Members> members, Sprint runningSprint){
        ArrayNode datas = Json.newArray();
        for(Members member : members) {
            if (member.getMemberType() == Members.EnumMem.DEVELOPER) {
                Integer time = 0;
                ObjectNode membersJson = Json.newObject();
                List<MembersTasksDoing> memTasksDoing = member.getMembersTaskDoings();
                List<MembersTasksReview> memTasksReview = member.getMembersTasksReviews();
                String name = member.getAssociations().getUsers().getFirstName();
                for (MembersTasks memTaskD : memTasksDoing) {
                    if (memTaskD.getSprintTasks().getSprint().getId() == runningSprint.getId()) {
                        time = time + memTaskD.getTimeSpent();
                    }
                }
                for (MembersTasks memTaskR : memTasksReview) {
                    if (memTaskR.getSprintTasks().getSprint().getId() == runningSprint.getId()) {
                        time = time + memTaskR.getTimeSpent();
                    }
                }
                if (member.getActive() || time > 0 ) {
                    membersJson.put(name, time);
                    datas.add(membersJson);
                }
            }
        }

        return datas;
    }

    public static Integer getTimes(List<Members> members, Sprint sprint){
        return getTimesReview(members, sprint)+getTimesDoing(members,sprint);
    }

    public static Integer getTimesDoing(List<Members> members, Sprint sprint){
        Integer time = 0;
        for(Members member : members) {
            if (member.getMemberType() == Members.EnumMem.DEVELOPER) {
                List<MembersTasksDoing> memTasksD = member.getMembersTaskDoings();
                for (MembersTasks memTask : memTasksD) {
                    if (memTask.getSprintTasks().getSprint().getId() == sprint.getId()) {
                        time = time + memTask.getTimeSpent();
                    }
                }
            }
        }
        return time;
    }


    public static Integer getTimesReview(List<Members> members, Sprint sprint){
        Integer time = 0;
        for(Members member : members) {
            if (member.getMemberType() == Members.EnumMem.DEVELOPER) {
                List<MembersTasksReview> memTasksR = member.getMembersTasksReviews();
                for (MembersTasks memTask : memTasksR) {
                    if (memTask.getSprintTasks().getSprint().getId() == sprint.getId()) {
                        time = time + memTask.getTimeSpent();
                    }
                }
            }
        }
        return time;
    }

    public ObjectNode getDatasBurnDown(Sprint sprint){
        ObjectNode obj = Json.newObject();
        Integer count = 0;
        List<SprintTasks> tasks = sprint.getTasks();
        List<SprintTasks> tasksDone = sprint.getTasksDone();
        obj.put("0", 0);
        Calendar ancDate = Calendar.getInstance();
        if(!tasksDone.isEmpty()) {
            ancDate.setTime(tasksDone.get(0).getEndDate());
            SprintTasks lastTaskDone = tasksDone.get(tasksDone.size()-1);
            long duration = Dates.dayDiff(sprint.getStartDate(), lastTaskDone.getEndDate());
            obj.put("0", tasks.size());

            for (int i = 1; i < duration; i++) {
                for (SprintTasks task : tasksDone) {
                    if (sameDay(task.getEndDate(), ancDate)) {
                        count++;
                    }
                }
                obj.put(Integer.toString(i), tasks.size() - count);
                ancDate.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return obj;
    }

    private Boolean sameDay(Date d1, Calendar cal2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

