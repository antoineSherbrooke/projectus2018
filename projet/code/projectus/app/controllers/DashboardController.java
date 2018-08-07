package controllers;

import Security.Developer;
import Security.Secure;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.MembersTasks;
import models.*;
import models.SprintTasks.EnumState;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.dashboard;
import views.html.dashboard_empty;
import views.html.dashboard_task;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@With(Secure.class)
public class DashboardController extends Controller {

    @Inject FormFactory formFactory;
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


	public Result dashboard(){
	    ReleasesController.endReleaseIfPasted();
        Sprint sprintInRunning = Sprint.sprintRunning(SessionController.getProjectRunning());
		if(sprintInRunning == null){
			return ok(dashboard_empty.render());
		}
        int timeWorked = sprintInRunning.getTimeWorked();
        int totalRemaningTime = sprintInRunning.getTotalRemainingTime();
        int percent = sprintInRunning.percentOfRemainingDay();

        if(percent > 100){
            return ok(dashboard_empty.render());
        }

		return ok(dashboard.render(sprintInRunning,percent,"Remaining : "+sprintInRunning.toStringRemainingTime(), sprintInRunning.percentTimeWorked(timeWorked, totalRemaningTime), timeWorked, totalRemaningTime));
	}


	public Result infoTask(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int id = Integer.parseInt(requestData.get("id_task"));
		SprintTasks task = SprintTasks.find.byId(id);
        MembersTasksDoing membersTasksDoingDoing = (MembersTasksDoing) getMemberOfTask(MembersTasksDoing.class, task);
        MembersTasksReview membersTasksReview = (MembersTasksReview) getMemberOfTask(MembersTasksReview.class, task);
        return ok(dashboard_task.render(task, membersTasksDoingDoing, membersTasksReview, true));
	}
	
	public Result addComment() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int idTask = Integer.parseInt(requestData.get("id_task"));
        String text = requestData.get("text");
        ObjectNode result = Json.newObject();
        Members member = SessionController.getMember();
        Users user = SessionController.getUsers();
        SprintTasks task = Ebean.find(SprintTasks.class).where().eq("id", idTask).findUnique();
        Date date = new Date();
        Comments comment = new Comments("test", text, date, task, member);
        comment.save();
        result.put("member_name", user.getFirstName());
        result.put("date", DATE_FORMAT.format(date));
        if (SessionController.isSupervisor()) {
            result.put("member_status", member.getMemberType().toString());
        }else{
            result.put("member_status", member.getMemberType().toString());
        }
        return ok(result);
	}

	private Object getMemberOfTask(Class member, SprintTasks task){
		return Ebean.find(member).where().and(Expr.eq("members_id", SessionController.getMemberId()), Expr.eq("sprint_tasks_id", task.getId()))
				.setMaxRows(1)
				.findUnique();
	}

    public Boolean workingAlready(EnumState stateToGo, SprintTasks task){
        Boolean workingAlready = false;
        if((stateToGo == EnumState.TOREVIEW || stateToGo == EnumState.DOING) && !((task.getState() == EnumState.DOING && stateToGo == EnumState.TOREVIEW) || (task.getState() == EnumState.TOREVIEW && stateToGo == EnumState.DOING))) {
            workingAlready = task.getSprint().getBacklogEntries().stream().anyMatch(BacklogEntries::workingAlready);
        }
        return workingAlready;
    }

    @With(Developer.class)
    public Result editState(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int id =  Integer.parseInt(requestData.get("id_task"));
        int state = Integer.parseInt(requestData.get("state"));

        Boolean force = "true".equals(requestData.get("force"));

        SprintTasks task = SprintTasks.find.byId(id);
        if (task == null) {
            return badRequest("Invalid task id");
        }

        ObjectNode result = Json.newObject();
        EnumState stateToGo = EnumState.values()[state];

        Boolean isDoing = (stateToGo == EnumState.TOREVIEW && task.getState() == EnumState.DOING);
        Boolean isToReview = (stateToGo == EnumState.DONE && task.getState() == EnumState.TOREVIEW);

        if (workingAlready(stateToGo, task)) {
            return badRequest("You can't working on two tasks");
        }
        if (isDoing) {
            if (taskNotWorked(task) ) {
                return badRequest("You must log your hours!");
            } else if (taskNotCompleted(task)) {
                return badRequest("Task has remaining time");
            }
        } else if (isToReview) {
            if (taskReviewNotWorked(task) ) {
                return badRequest("You must log your hours");
            } else if (taskReviewNotCompleted(task)) {
                return badRequest("Review has remaining time");
            }
        }

		if(!ifSkipStep(state,task))
            return badRequest("You can't skip steps");

        if(reopeningTask(stateToGo, task)){
            task.setRemainingTime(task.getRemainingTime()+60);
            result.put("doing_progress", task.getWorkingTime()*100/task.getRemainingTime());
        }

        if(stateToGo == EnumState.TODO){

            if(!canMove(task) && !force){
                return badRequest("It's not your task ! ");
            }
            if(canBackTODO(task)){
                MembersTasksDoing memTask = MembersTasksDoing.find.where().and(Expr.eq("sprint_tasks_id", task.getId()), Expr.eq("members_id", SessionController.getMember().getId())).findUnique();
                if(memTask != null)  {
                    memTask.delete();
                }
            }else {
                return badRequest("You can't do this, you have logged hours or you are not alone on this task !");
            }
        }
        task.setState(stateToGo);
        if(stateToGo == EnumState.DONE){
            task.setEndDate(new Date());
        }
        if(stateToGo != EnumState.DONE && stateToGo != EnumState.TODO){
            takeTaskDoing(task, result);
        }
        task.update();
        result.put("nb_dev", nbMemberOfTask(task, stateToGo));

        if(task.getSprint().allTasksDone()){
            SprintsController.setSprintState(task.getSprint().getId(), Sprint.State.FINISHED);
            result.put("feature_finish", true);
        }

		return ok(result);
	}

    @With(Developer.class)
    public Result giveUp(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int id =  Integer.parseInt(requestData.get("id_task"));
        int state = Integer.parseInt(requestData.get("state"));
        SprintTasks task = SprintTasks.find.byId(id);
        if (task == null) {
            return badRequest("Invalid task id");
        }
        ObjectNode result = Json.newObject();
        EnumState enumState = EnumState.values()[state];
        result.put("change", false);
        if(enumState == EnumState.DOING){
            MembersTasksDoing memTaskD = MembersTasksDoing.find.where().and(Expr.eq("sprint_tasks_id", task.getId()), Expr.eq("members_id", SessionController.getMember().getId())).findUnique();
            if (memTaskD == null) {
                return badRequest("Invalid request");
            }
            if(memTaskD.getTimeSpent() > 0){
                return badRequest("You can't do this, you have logged hours !");
            }else {
                memTaskD.delete();
                if(task.getMembersTaskDoings().isEmpty()){
                    result.put("change", true);
                    result.put("stateToGo", EnumState.TODO.getValue());
                }
            }
        }else if(enumState == EnumState.TOREVIEW){
            MembersTasksReview memTaskR = MembersTasksReview.find.where().and(Expr.eq("sprint_tasks_id", task.getId()), Expr.eq("members_id", SessionController.getMember().getId())).findUnique();
            if (memTaskR == null) {
                return badRequest("Invalid request");
            }
            if(memTaskR.getTimeSpent() != 0){
                return badRequest("You can't do this, you have logged hours !");
            }else {
                memTaskR.delete();
                if(task.getMembersTasksReviews().isEmpty()){
                    result.put("change", false);
                }
            }
        }

        return ok(result);
    }

    private Boolean taskNotWorked(SprintTasks task){
        return (task.getWorkingTime() == 0);
    }
    private Boolean taskReviewNotWorked(SprintTasks task){
        return (task.getWorkingTimeReview() == 0);
    }
    private Boolean taskNotCompleted(SprintTasks task) {
        return (task.getRemainingTime() == 0) || (task.getWorkingTime() != task.getRemainingTime());
    }
    private Boolean taskReviewNotCompleted(SprintTasks task) {
        return (task.getRemainingTimeReview() == 0) || (task.getWorkingTimeReview() != task.getRemainingTimeReview());
    }

    private Boolean ifSkipStep(int state, SprintTasks task){
        return Math.abs(task.getState().getValue()-1-state) == 1;
    }

    private Boolean reopeningTask(EnumState stateToGo, SprintTasks task){
        return stateToGo == EnumState.DOING && task.getState() == EnumState.TOREVIEW;
    }

    private Boolean canMove(SprintTasks task){
        Boolean result = false;
        for(MembersTasksDoing memTask : task.getMembersTaskDoings()){
            if(memTask.getMembers().getId() == SessionController.getMember().getId()){
                result = true;
            }
        }
        return result;
    }

    private Boolean canBackTODO(SprintTasks task){
        Boolean result = true;
        for(MembersTasksDoing memTask : task.getMembersTaskDoings()){
            if(memTask.getTimeSpent() != 0){
                result = false;
            }
        }
        if(task.getMembersTaskDoings().size() > 1){
            result = false;
        }
        return result;
    }

    private int nbMemberOfTask(SprintTasks task, EnumState stateToGo){
        task.refresh();
        if(stateToGo == EnumState.DOING){
            return task.getMembersTaskDoings().size();
        }else if(stateToGo == EnumState.TOREVIEW){
            return task.getMembersTasksReviews().size();
        }
        return (task.getMembersTasksReviews().size() + task.getMembersTaskDoings().size());

    }

    @With(Developer.class)
	public Result takeTask(Integer id){
		SprintTasks task = SprintTasks.find.byId(id);
        if (task == null) {
            return badRequest("Invalid task id");
        }
        ObjectNode result = Json.newObject();

        if (workingAlready(task.getState(), task)) {
            return badRequest("You can't working on two tasks");
        }
        result.put("firstName", session("firstName"));
        result.put("lastName", session("lastName"));

		if(task.getState() == EnumState.DOING){
			return takeTaskDoing(task, result);
		}else if(task.getState() == EnumState.TOREVIEW){
            return takeTaskReview(task, result);
        }else{
            return badRequest("It's impossible to take a task not in doing");
		}
    }


    private Boolean existMemberInTheTask(SprintTasks task, Class memberClass){
        return getMemberOfTask(memberClass, task) != null;
    }

	private Result takeTaskReview(SprintTasks task, ObjectNode result){
        if(!existMemberInTheTask(task, MembersTasksReview.class)){
			MembersTasksReview membersTasks = new MembersTasksReview(0,new Date(),task,SessionController.getMember());
			membersTasks.save();
            History.takeTaskReview(task);
			result.put("nb_dev",task.getMembersTasksReviews().size());
		}else{
            return badRequest("The member is already on the task !");
		}
		return ok(result);
	}

	private Result takeTaskDoing(SprintTasks task, ObjectNode result){
        if(!existMemberInTheTask(task, MembersTasksDoing.class)){
			MembersTasksDoing membersTasksDoing = new MembersTasksDoing(0,new Date(),task,SessionController.getMember());
            task.setStartDate(new Date());
            task.update();
			membersTasksDoing.save();
            History.takeTaskDoing(task);
			result.put("nb_dev",task.getMembersTaskDoings().size());
		}else{
            return badRequest("The member is already on the task !");
		}
		return ok(result);
	}

    private Result editTimeWorked(Integer id_task, Integer worked, Integer remainingTime, Class memberClass){
        SprintTasks task = SprintTasks.find.byId(id_task);
        if (task == null) {
            return badRequest("Invalid task id");
        }
        MembersTasks membersTasks = (MembersTasks) getMemberOfTask(memberClass, task);
        if(memberClass == MembersTasksDoing.class){
            task.setRemainingTime(remainingTime);
        }else{
            task.setRemainingTimeReview(remainingTime);
        }
        membersTasks.setTimeSpent(worked);
        task.update();
        membersTasks.update();
        return ok("Edit time worked with success");
    }

    @With(Developer.class)
	public Result editTimeWorkedOnTheTaskDoing(Integer id_task, Integer worked, Integer remainingTime){
		return editTimeWorked(id_task, worked, remainingTime, MembersTasksDoing.class);
	}

    @With(Developer.class)
    public Result editTimeWorkedOnTheTaskReview(Integer id_task, Integer worked, Integer remainingTime){
        return editTimeWorked(id_task, worked, remainingTime, MembersTasksReview.class);
    }
}
