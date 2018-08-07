package models;

import com.avaje.ebean.Model;
import controllers.SessionController;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class History extends Model{

	final static DateFormat format = new SimpleDateFormat("EEEE d MMM YYYY, hh:mm aaa", Locale.ENGLISH);

	@Id
	private Integer id;

	private String description;
    private Date date;

	@ManyToOne
	private Projects projects;

    @ManyToOne
    private Members members;

	private History(String description){
        this.description = description;
        this.date = new Date();
        this.projects = SessionController.getProjectRunning();
        this.members = SessionController.getMember();
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public String dateFormat(Date date){
        return format.format(date);
    }

    private static String getName(){
        Members member = SessionController.getMember();
        return " by "+ member.getAssociations().getUsers().getFirstName()+" "+member.getAssociations().getUsers().getLastName();

    }

    //RELEASES
    public static History newRelease(Releases release){
        History history = new History(genStringNew("release", release.getName()));
        history.save();
        return history;
    }

    public static History editRelease(Releases release){
        History history = new History(genStringEDA("release", release.getName(), "edited"));
        history.save();
        return history;
    }

    public static History disableRelease(Releases release){
        History history = new History(genStringEDA("release", release.getName(), "disabled"));
        history.save();
        return history;
    }

    public static History activateRelease(Releases release){
        History history = new History(genStringEDA("release", release.getName(), "activated"));
        history.save();
        return history;
    }

    //SPRINTS
    public static History newSprint(Sprint sprint){
        History history = new History(genStringNewInside("sprint", sprint.getName(), "Release", sprint.getReleases().getName()));
        history.save();
        return history;
    }

    public static History editSprint(Sprint sprint){
        History history = new History(genStringEDAInside("sprint", sprint.getName(), "Release", sprint.getReleases().getName(), "edited"));
        history.save();
        return history;
    }

    public static History finishSprint(Sprint sprint){
        History history = new History(genStringEDAInside("sprint", sprint.getName(), "Release", sprint.getReleases().getName(), "finished"));
        history.save();
        return history;
    }

    //BACKLOGS
    public static History newFeature(BacklogEntries feature){
        History history = new History(genStringNew("feature", feature.getName()));
        history.save();
        return history;
    }

    public static History editFeature(BacklogEntries feature){
        History history = new History(genStringEDA("feature", feature.getName(), "edited"));
        history.save();
        return history;
    }

    public static History assignFeature(BacklogEntries feature, Sprint sprint){
        History history = new History(genStringAssign("feature", feature.getName(), "sprint", sprint.getName()));
        history.save();
        return history;
    }

    //TASKS
    public static History newTask(SprintTasks task){
        History history = new History(genStringNewInside("task", task.getName(), "Feature", task.getBacklogEntries().getName()));
        history.save();
        return history;
    }

    public static History editTask(SprintTasks task){
        History history = new History(genStringEDAInside("task", task.getName(), "Feature", task.getBacklogEntries().getName(), "edited"));
        history.save();
        return history;
    }

    public static History finishTask(SprintTasks task){
        History history = new History(genStringEDAInside("task", task.getName(), "Feature", task.getBacklogEntries().getName(), "finished"));
        history.save();
        return history;
    }

    public static History takeTaskDoing(SprintTasks task){
        History history = new History("<div class='history1' style=''>The task <strong>"+task.getName()+"</strong> (Feature: "+task.getBacklogEntries().getName()+") has been taken"+getName()+"</div>");
        history.save();
        return history;
    }

    public static History takeTaskReview(SprintTasks task){
        History history = new History("<div class='history1' style=''>The task <strong>"+task.getName()+"</strong> (Feature : "+task.getBacklogEntries().getName()+") has been taken"+getName()+" for review</div>");
        history.save();
        return history;
    }

    //MEMBERS
    public static History newMember(Members member){
        History history = new History(genStringNew("member", member.getAssociations().getUsers().getFirstName()+" "+member.getAssociations().getUsers().getLastName()));
        history.save();
        return history;
    }

    public static History disableMember(Members member){
        History history = new History(genStringEDA("member", member.getAssociations().getUsers().getFirstName()+" "+member.getAssociations().getUsers().getLastName(), "disabled"));
        history.save();
        return history;
    }

    public static History activateMember(Members member){
        History history = new History(genStringEDA("member", member.getAssociations().getUsers().getFirstName()+" "+member.getAssociations().getUsers().getLastName(), "activated"));
        history.save();
        return history;
    }

    //MEETING
    /*public static History newSprintMeeting(Meetings meeting){
        History history = new History(dateFormat()+" : A new meeting "+meeting.getName()+" has been created");
        history.save();
        return history;
    }

    public static History concludeSprintMeeting(Meetings meeting){
        History history = new History(dateFormat()+" : The member "+meeting.getName()+" has been concluded"+getName());
        history.save();
        return history;
    }*/

    public static History newMeeting(Meetings meeting){
        History history = new History("A new meeting "+meeting.getName()+" has been created"+getName());
        history.save();
        return history;
    }



    public static String genStringNew(String type, String name){

        return "<div class='history1' style=''>A new "+type+" <strong>"+name+"</strong> has been created"+getName()+"</div>";
    }

    public static String genStringEDA(String type, String name, String action){

        return  "<div class='history1' style=''>The "+type+" <strong>"+name+"</strong> has been "+action+getName()+"</div>";
    }

    public static String genStringAssign(String type, String name, String assignType, String assignName){

        return  "<div class='history1' style=''>The "+type+" <strong>"+name+"</strong> has been assigned to the "+assignType+" "+assignName+getName()+"</div>";
    }

    public static String genStringNewInside(String type, String name, String insideType, String insideName){

        return "<div class='history1' style=''>A new "+type+" <strong>"+name+"</strong> ("+insideType+" : "+insideName+") has been created"+getName()+"</div>";
    }

    public static String genStringEDAInside(String type, String name, String insideType, String insideName, String action){

        return "<div class='history1' style=''>The "+type+" <strong>"+name+"</strong> ("+insideType+" : "+insideName+") has been "+action+getName()+"</div>";
    }
    public static Finder<Integer, History> find = new Finder<>(History.class);
}
