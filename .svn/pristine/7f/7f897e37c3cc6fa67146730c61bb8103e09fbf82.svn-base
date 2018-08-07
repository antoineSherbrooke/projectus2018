package models;

import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
public class Members extends Model{

    public static int compare(Members m1, Members m2) {
        return m1.getMemberType().getValue() - m2.getMemberType().getValue();
    }

    @Id
    private Integer id;

    private Boolean news;
    private Boolean active;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Associations associations;

    @ManyToOne(cascade = CascadeType.ALL)
    private Groups groups;

    @OneToMany
    private List<Comments> comments;

    @OneToMany
    private List<MembersTasksDoing> membersTaskDoings;

    @OneToMany
    private List<MembersTasksReview> membersTasksReviews;

    @OneToMany
    private List<History> histories;

    private EnumMem memberType;

    private int lastHistorySeen;

    public Members(Associations associations, Groups groups, EnumMem type){
        this.associations = associations;
        this.groups = groups;
        this.memberType = type;
        this.active=true;
    }
    
    public Members(Boolean b, EnumMem type){
        this.news = b;
        this.memberType = type;
    }
    public Members(Boolean b, EnumMem type,Associations associations,Groups groups,List<Comments> comments,List<MembersTasksDoing> membersTaskDoings){
        this.news = b;
        this.memberType = type;
        this.associations = associations;
        this.comments = comments;
        this.membersTaskDoings = membersTaskDoings;
        this.groups = groups;
    }

    public List<History> getHistories() {
        return histories;
    }

    public List<History> getHistoriesReverse() {
        List<History> histories = getHistories();
        Collections.reverse(histories);
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<MembersTasksReview> getMembersTasksReviews() {
        return membersTasksReviews;
    }

    public void setMembersTasksReviews(List<MembersTasksReview> membersTasksReviews) {
        this.membersTasksReviews = membersTasksReviews;
    }

    public List<MembersTasksDoing> getMembersTaskDoings() {
        return membersTaskDoings;
    }



    public void setMembersTaskDoings(List<MembersTasksDoing> membersTaskDoings) {
        this.membersTaskDoings = membersTaskDoings;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMemberType(EnumMem type) {
        this.memberType = type;
    }

    public Associations getAssociations() {
        return associations;
    }

    public void setAssociations(Associations associations) {
        this.associations = associations;
    }


    public EnumMem getMemberType() {
        return memberType;
    }


    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SprintTasks getMemberTaskRunning(){
        SprintTasks sprintTask = null;
        for(MembersTasksDoing taskDoing :  membersTaskDoings){
            if(taskDoing.getSprintTasks().getState() == SprintTasks.EnumState.DOING){
                sprintTask = taskDoing.getSprintTasks();
            }
        }
        for(MembersTasksReview taskReview :  membersTasksReviews){
            if(taskReview.getSprintTasks().getState() == SprintTasks.EnumState.TOREVIEW){
                sprintTask = taskReview.getSprintTasks();
            }
        }
        return sprintTask;
    }

    public int getLastHistorySeen() {
        return lastHistorySeen;
    }

    public void setLastHistorySeen(int lastHistorySeen) {
        this.lastHistorySeen = lastHistorySeen;
    }

    public enum EnumMem {
        PRODUCT_OWNER(1), SCRUM_MASTER(2), ADMINISTRATOR(3), DEVELOPER(4);
        private int value;

        EnumMem(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Users getUser() {
        return getAssociations().getUsers();
    }

    public static Finder<Integer, Members> find = new Finder<Integer,Members>(Members.class);

}
