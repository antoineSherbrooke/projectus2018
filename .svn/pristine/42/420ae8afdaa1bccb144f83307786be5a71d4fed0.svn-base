package models;

import com.avaje.ebean.Model;
import models.Members;
import models.SprintTasks;
import utils.Dates;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
abstract public class MembersTasks extends Model {

    @Id
    protected int id;

    protected int timeSpent;
    protected Date day;

    @ManyToOne
    protected SprintTasks sprintTasks;

    @ManyToOne
    protected Members members;

    public MembersTasks(int t, Date d){
        this.timeSpent = t;
        this.day = d;
    }

    public MembersTasks(int t, Date d, SprintTasks st, Members m){
        this.timeSpent = t;
        this.day = d;
        this.sprintTasks = st;
        this.members = m;
    }

    public String timeFormat(Integer time){
        return Dates.toStringTimeFormat(time);
    }

    public abstract int getOthersTimeSpent();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getDay(){
        return day;
    }
    public void setDay(Date d){
        this.day = d;
    }
    public int getTimeSpent() {
        return timeSpent;
    }
    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    public Members getMembers() {
        return members;
    }
    public void setMembers(Members members){
        this.members = members;
    }

    public SprintTasks getSprintTasks() {
        return sprintTasks;
    }
    public void setSprintTasks(SprintTasks sprintTasks) {
        this.sprintTasks = sprintTasks;
    }
}
