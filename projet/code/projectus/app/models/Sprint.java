package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.time.DateUtils;
import play.data.validation.Constraints.Required;
import utils.Dates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


@Entity
public class Sprint extends Model implements DateShiftable {
    private final static DateFormat format = new SimpleDateFormat("EEEE, d MMM, YYYY", Locale.ENGLISH);

    @Id
    private Integer id;
    @Required
    private String name;

    private Date startDate;
    private Date endDate;
    private String description;
    private int number; //FIXMETOREMOVE @ next database change (unused)
    private State state;

    @ManyToOne
    private Releases releases;

    @ManyToMany
    private List<BacklogEntries> backlogEntries;

    @OneToMany
    private List<Meetings> meeting;

    public Sprint() {
        this("unnamed sprint", new Date(), new Date());
    }

    public Sprint(String name, Releases releases) {
        this(name, new Date(), new Date(), releases, "");
    }

    public Sprint(String name, Date start, Date end) {
        this(name, start, end, new Releases(), "");
    }

    public Sprint(String name, Date start, Date end, Releases releases, String description) {
        this.name = name;
        this.setStartDate(start);
        this.setEndDate(end);
        this.releases = releases;
        this.description = description;
        this.state = State.INACTIVE;
        this.backlogEntries = new ArrayList<>();
    }

    public static Sprint sprintRunning(Projects project){
        Sprint sprintsInRunning = null;
        try {
            for (Releases releases : project.getReleases()) {
                try {
                    sprintsInRunning = releases.getSprint().stream()
                            .filter(sprint -> sprint.getState() == State.RUNNING).findFirst().orElse(null);

                } catch (Exception ignored) {}

                if (sprintsInRunning != null) {
                    break;
                }
            }
        }catch(Exception ignored){}
        return sprintsInRunning;
    }

    @Override
    public void shiftDatesBy(int milliseconds) {
        setExactStartDate(DateUtils.addMilliseconds(startDate, milliseconds));
        setExactEndDate(DateUtils.addMilliseconds(endDate, milliseconds));
    }

    public enum State {
        INACTIVE(0), RUNNING(1), FINISHED(2), ABORTED(3);
        private int value;
        State(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public String getColor(){
            String[] colors = {"primary", "primary", "success", "danger"};
            return colors[this.value];
        }
    }

    public void addBacklogEntrie(BacklogEntries backlogEntries) {
        this.backlogEntries.add(backlogEntries);
    }

    public void reverseFeatures(){
        backlogEntries = Lists.reverse(backlogEntries);
    }

    public Boolean isRunning() {
        return state == State.RUNNING;
    }

    public Boolean isInactive() {
        return state == State.INACTIVE;
    }

    public Integer getTotalTime(){
        return getTotalTimeDoing() + getTotalTimeReview();
    }

    public Integer getTotalTimeDoing(){
        Integer time = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                time = time + task.getEstimate();
            }
        }
        return time;
    }

    public Integer getTotalTimeReview(){
        Integer time = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                time = time + task.getEstimateReview();
            }
        }
        return time;
    }

    public Integer getRemainingTime(){
        return getTotalRemainingTime() - getTimeWorked() ;
    }

    public Integer getTotalRemainingTime(){
        Integer time = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                time = time + task.getRemainingTime() + task.getRemainingTimeReview();
            }
        }
        return time;
    }

    public Integer getRemainingTimeEstimate(){
        return getTotalTime() - getTimeWorked() ;
    }

    public Integer getDayNumber(){
        Date today = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(today);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(startDate);
        return cal1.get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR) + 1;
    }

    public long getRemainingHours(){
        Date today = new Date();
        return ((endDate.getTime() - today.getTime()) / (1000 * 60 * 60));
    }

    private long getRemainingMilliseconds(){
        Date today = new Date();
        return (endDate.getTime() - today.getTime());
    }

    public String toStringRemainingTime(){
        long remaining = getRemainingMilliseconds();
        String remainingString = "";
        if ((remaining / (24*60*60*1000) ) > 1) {
            remainingString = Dates.millisecondsToString(remaining, Dates.Unit.HOURS);
        } else {
            remainingString = Dates.millisecondsToString(remaining, Dates.Unit.MINUTES);
        }
        return remainingString;
    }

    public Integer getTimeWorked(){
        int timeWorked = 0;
            for (Members membersLoop : getReleases().getProjects().getGroups().getMembers()) {
                if (membersLoop.getMemberType() == Members.EnumMem.DEVELOPER) {
                    List<MembersTasksDoing> membersTasksDoingLists = membersLoop.getMembersTaskDoings();
                    for ( MembersTasksDoing tasksDoingLoop : membersTasksDoingLists) {
                        if (tasksDoingLoop.getSprintTasks().getSprint() == this)
                            timeWorked = timeWorked + tasksDoingLoop.getTimeSpent();
                    }
                    List<MembersTasksReview> membersTasksReviewsLoop = membersLoop.getMembersTasksReviews();
                    for ( MembersTasksReview tasksReviewLoop : membersTasksReviewsLoop) {
                        if (tasksReviewLoop.getSprintTasks().getSprint() == this)
                            timeWorked = timeWorked + tasksReviewLoop.getTimeSpent();
                    }
                }
            }
        return timeWorked;
    }

    private long getNumberOfHour(){
        return ( (Dates.getEndOfDay(endDate).getTime() - Dates.getStartOfDay(startDate).getTime()) / (1000 * 60 * 60) );
    }

    public Integer percentOfRemainingDay(){
        int percent = 0;
        try {
            percent = 100 - ((int) ((getRemainingHours() * 100) / getNumberOfHour()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return percent;
    }

    public Integer percentTimeWorked(int timeWorked, int totalRemaingTime){
        return (timeWorked*100) / (totalRemaingTime);
    }

    public String hourFormat(Integer value){
        String hour;
        if (value >= 0) {
            hour = Dates.toStringTimeFormat(value);
        }
        else {
            hour = "Additionnal time : " + Dates.toStringTimeFormat(-value);
        }
        return hour;
    }

    public Integer getNumCompletedTasks(){
        Integer count = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                if(task.getState() == SprintTasks.EnumState.DONE){
                    count = count + 1;
                }
            }
        }
        return count;
    }

    public Integer getNumInProgressTasks(){
        Integer count = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                if(task.getState() == SprintTasks.EnumState.DOING){
                    count = count + 1;
                }
            }
        }
        return count;
    }

    public Integer getNumNotStartedTasks(){
        Integer count = 0;
        for(BacklogEntries feature: backlogEntries){
            for(SprintTasks task : feature.getTasksInSprintAndFeature(this)){
                if(task.getState() == SprintTasks.EnumState.TODO){
                    count = count + 1;
                }
            }
        }
        return count;
    }

    public List<BacklogEntries> getActivatedBacklogEntries(){
        List<BacklogEntries> activatedFeatures = new ArrayList<>();
        for(BacklogEntries feature: backlogEntries){
            if(feature.getActivate()){
                activatedFeatures.add(feature);
            }
        }
        return activatedFeatures;
    }

    public List<BacklogEntries> getBacklogEntriesByNumber(){
        List<BacklogEntries> backlogEntries = getActivatedBacklogEntries();

        Collections.sort(backlogEntries, (o1, o2) -> o1.getNumber() - o2.getNumber());
        return backlogEntries;
    }

    public Boolean allTasksDone() {
        Boolean sprintFinish = true;
        for (BacklogEntries backlogEntries : getBacklogEntries()) {
            sprintFinish = sprintFinish &&
                    backlogEntries.getTasksInSprintAndFeature(this).stream()
                            .allMatch(tasks -> tasks.getState() == SprintTasks.EnumState.DONE);
        }
        return sprintFinish;
    }

    public Boolean isNotEmpty(){
        for(BacklogEntries backlogEntries : getBacklogEntries()){
            if(!backlogEntries.getTasksInSprintAndFeature(this).isEmpty()){
                return true;
            }
        }
        return false;
    }

    private Boolean isPast() {
        return getEndDate().before(new Date());
    }

    public Boolean isEditable(){
        boolean isEditable = false;
        if (state == State.INACTIVE) {
            isEditable = !isPast() && !getReleases().isPast();
        } else if (state == State.RUNNING && getReleases().getRunning()) {
            Date now = new Date();
            isEditable = now.before(DateUtils.addHours(getStartDate(),24));
        }
        return isEditable;
    }

    public List<SprintTasks> getTasks(){
        ArrayList<SprintTasks> returnList = new ArrayList<>();
        for (BacklogEntries featureLoop : backlogEntries) {
            for (SprintTasks tasksLoop : featureLoop.getSprintTasks()) {
                if (tasksLoop.getSprint() == this) {
                    returnList.add(tasksLoop);
                }
            }
        }
        return returnList;
    }

    public List<SprintTasks> getTasksDone(){
        ArrayList<SprintTasks> returnList = new ArrayList<>();
        for (SprintTasks tasksLoop : getTasks()) {
            if (tasksLoop.getState() == SprintTasks.EnumState.DONE) {
                returnList.add(tasksLoop);
            }
        }
        return returnList;
    }

    public void cancelRemainingDoingTasks(){
        List<SprintTasks> tasksDoing = getTasks();
        for(SprintTasks taskDoing : tasksDoing){
            if(taskDoing.getState() == SprintTasks.EnumState.DOING || taskDoing.getState() == SprintTasks.EnumState.TOREVIEW){
                taskDoing.setState(SprintTasks.EnumState.TODO);
                taskDoing.update();
            }
        }
    }

    public String dateFormat(Date date){
        if(date == null){
            return "";
        }
        return format.format(date);
    }

    public void setStartDate(Date startDate) {
        this.startDate = Dates.getStartOfDay(startDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = Dates.getEndOfDay(endDate);
    }

    public void setExactStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setExactEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static Finder<Integer, Sprint> find = new Finder<>(Sprint.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Releases getReleases() {
        return releases;
    }

    public void setReleases(Releases releases) {
        this.releases = releases;
    }

    public List<BacklogEntries> getBacklogEntries() {
        return backlogEntries;
    }

    public void setBacklogEntries(List<BacklogEntries> backlogEntries) {
        this.backlogEntries = backlogEntries;
    }

    public List<Meetings> getMeeting() {
        return meeting;
    }

    public void setMeeting(List<Meetings> meeting) {
        this.meeting = meeting;
    }
}
