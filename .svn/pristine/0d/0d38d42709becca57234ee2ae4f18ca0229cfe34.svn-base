package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import controllers.SessionController;
import play.data.validation.Constraints.Required;
import utils.Dates;

@Entity
public class SprintTasks extends Model {

    final static DateFormat format = new SimpleDateFormat("EEEE, d MMM, YYYY, hh:mm aaa", Locale.ENGLISH);

    @Id
    private Integer id;

    @Required
    private String name;

    private String description;
    private int estimate;
    private int remainingTime;
    private int remainingTimeReview;
    private int estimateReview;
    private Date startDate;
    private Date endDate;

    @OneToMany
    private List<Comments> comments;

    @OneToMany
    private List<MembersTasksDoing> membersTaskDoings;

    @OneToMany
    private List<MembersTasksReview> membersTasksReviews;

    @ManyToOne
    private BacklogEntries backlogEntries;

    @ManyToOne
    private Sprint sprint;

    private EnumState state;

    public enum EnumState {
        TODO(1), DOING(2), TOREVIEW(3), DONE(4);
        private static final String[] lowercase = {"todo", "doing", "toreview", "done"};
        private int value;

        EnumState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        public String getLowerCase() {
            return lowercase[this.value-1];
        }
    }

    public SprintTasks(String name, EnumState state, BacklogEntries feature, int firstEstimate){
        this(name, state, feature, firstEstimate, null, "...");
    }

    public SprintTasks(String name, EnumState state, String description, int firstEstimate){
        this(name, state, null, firstEstimate, null, description);
    }

    public SprintTasks(String name, EnumState state, BacklogEntries feature, int firstEstimate,
                       Sprint sprint, String description){
        this.name = name;
        this.state = state;
        this.backlogEntries = feature;
        this.setEstimate(firstEstimate);
        this.setEstimateReview(15);
        this.sprint = sprint;
        this.description = description;
    }

    public SprintTasks(String name, EnumState state, BacklogEntries feature, int firstEstimate,
                       String desc, List<Comments> comments, List<MembersTasksDoing> membersTaskDoings) {
        this(name, state, feature, firstEstimate, null, desc);
        this.comments = comments;
        this.membersTaskDoings = membersTaskDoings;
    }

    public Integer getWorkingTime() {
        int sum = 0;
        for(MembersTasksDoing membersTasksDoing :this.getMembersTaskDoings()){
            sum = sum + membersTasksDoing.getTimeSpent();
        }
        return sum;
    }

    public Integer getWorkingTimeReview(){
        int sum = 0;
        for(MembersTasksReview membersTasks:this.getMembersTasksReviews()){
            sum = sum + membersTasks.getTimeSpent();
        }
        return sum;
    }

    public int getProgress(){
        int remainingTime = getRemainingTime();
        if (remainingTime != 0) {
            return 100*getWorkingTime()/remainingTime;
        } else {
            return 0;
        }
    }

    public int getProgressReview(){
        int remainingTime = getRemainingTimeReview();
        if (remainingTime != 0) {
            return 100*getWorkingTimeReview()/remainingTime;
        } else {
            return 0;
        }
    }

    public String dateFormat(Date date){
        if(date == null){
            return "";
        }
        return format.format(date);
    }

    public void setEstimate(int firstEstimate) {
        this.estimate = firstEstimate;
        this.setRemainingTime(firstEstimate);
    }

    public void setEstimateReview(int estimateReview) {
        this.estimateReview = estimateReview;
        this.setRemainingTimeReview(estimateReview);
    }

    public String timeFormat(Integer time){
        return Dates.toStringTimeFormat(time);
    }

    public static Finder<Integer, SprintTasks> find = new Finder<>(SprintTasks.class);


    public List<MembersTasksReview> getMembersTasksReviews() {
        return membersTasksReviews;
    }

    public void setMembersTasksReviews(List<MembersTasksReview> membersTasksReviews) {
        this.membersTasksReviews = membersTasksReviews;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public List<MembersTasksDoing> getMembersTaskDoings() {
        return membersTaskDoings;
    }

    public BacklogEntries getBacklogEntries() {
        return backlogEntries;
    }

    public EnumState getState() {
        return state;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimate() {
        return estimate;
    }

    public int getEstimateReview() {
        return estimateReview;
    }
    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getRemainingTimeReview() {
        return remainingTimeReview;
    }

    public void setRemainingTimeReview(int remainingTimeReview) {
        this.remainingTimeReview = remainingTimeReview;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void addComments(Comments comments) {
        this.comments.add(comments);
    }

    public void setMembersTaskDoings(List<MembersTasksDoing> membersTaskDoings) {
        this.membersTaskDoings = membersTaskDoings;
    }

    public void setBacklogEntries(BacklogEntries backlogEntries) {
        this.backlogEntries = backlogEntries;
    }

    public void setState(EnumState state) {
        this.state = state;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
