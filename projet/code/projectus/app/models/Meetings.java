package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Model;

@Entity
public class Meetings extends Model{

    @Id
    private Integer id;

    @Required
    private String name;

    private Date date;
    private String conclusion;
    private int timeWorked;
    private String dayOrder;
    private int firstEstimate;
    private boolean activate;

    @ManyToOne(cascade = CascadeType.ALL)
    private Projects projects;

    @ManyToOne(cascade = CascadeType.ALL)
    private Sprint sprint;

    @OneToMany
    private List<MeetingMembers> meetingMembers;

    public Meetings(String name, String conclusion) {
        this(name, false, null);
        this.conclusion = conclusion;
    }

    public Meetings(String name, boolean activate, Projects projects) {
        this(name, "", 15, activate, projects, null);
    }

    public Meetings(String name, String dayOrder, int firstEstimate, boolean activate, Projects projects, Sprint sprint) {
        this(name, dayOrder, firstEstimate, activate, projects, sprint, "", 0, new Date());
    }

    public Meetings(String name, String dayOrder, int firstEstimate, boolean activate,
                    Projects projects, Sprint sprint, String conclusion, int timeWorked, Date date) {
        this.name = name;
        this.dayOrder = dayOrder;
        this.firstEstimate = firstEstimate;
        this.activate = activate;
        this.projects = projects;
        this.sprint = sprint;
        this.conclusion = conclusion;
        this.timeWorked = timeWorked;
        this.date = date;
    }

    public static Finder<Integer, Meetings> find = new Finder<Integer,Meetings>(Meetings.class);

    public Integer getId() {
        return id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public List<MeetingMembers> getMeetingMembers() {
        return meetingMembers;
    }

    public void setMeetingMembers(List<MeetingMembers> meetingMembers) {
        this.meetingMembers = meetingMembers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTimeWorked() {
        return timeWorked;
    }

    public void setTimeWorked(int timeWorked) {
        this.timeWorked = timeWorked;
    }

    public String getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(String dayOrder) {
        this.dayOrder = dayOrder;
    }

    public int getFirstEstimate() {
        return firstEstimate;
    }

    public void setFirstEstimate(int firstEstimate) {
        this.firstEstimate = firstEstimate;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }


}