package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import com.avaje.ebean.Model;
import controllers.SessionController;
import play.data.validation.Constraints.Required;
import models.SprintTasks.EnumState;

@Entity
public class BacklogEntries extends Model {

    @Id
    private Integer id;

    @Required
    private String name;
    private String description;

    private int state;

    private int number;
    private Boolean activate;
    private EnumFirstEstimate firstEstimate;
    private int parentId;
    private EnumPriority priority;
    private int totalTime;


    @ManyToMany
    private List<Sprint> sprint;

    @ManyToOne(cascade = CascadeType.ALL)
    private Projects projects;

    @OneToMany
    private List<SprintTasks> sprintTasks;

    public BacklogEntries(String name, String description) {
        this.name = name;
        this.description = description;
        this.activate = true;
    }

    public BacklogEntries(String name, String description, EnumPriority priority, EnumFirstEstimate firstEstimate){
        this(name, description);
        this.priority = priority;
        this.firstEstimate = firstEstimate;
        this.activate = true;
    }
    public BacklogEntries(String name, String description, EnumPriority priority, EnumFirstEstimate firstEstimate, Projects projects){
        this(name, description, priority, firstEstimate);
        this.projects = projects;
        this.activate = true;
    }
    public BacklogEntries(String name, String description, Sprint sprint, int number, Projects projects) {
        this(name, description);

        this.number = number;
        this.projects = projects;
        this.activate = true;
    }
    public BacklogEntries(String name, String description, Sprint sprint, int number, Projects projects, EnumFirstEstimate firstEstimate, EnumPriority priority) {
        this(name, description, sprint, number, projects);
        this.firstEstimate = firstEstimate;
        this.priority = priority;
        this.activate = true;
    }
    public BacklogEntries(String f, String d, int s, int n, EnumFirstEstimate fe, int pa, EnumPriority pr, int t) {
        this(f, d, new Sprint(), n, new Projects(), fe, pr);
        this.state = s;
        this.parentId = pa;
        this.totalTime = t;
        this.activate = true;
    }

    public BacklogEntries(String f, String d, int s, int n, EnumFirstEstimate fe, int pa, EnumPriority pr, int t, List<SprintTasks> sprintTasks, Sprint sprint) {
        this(f, d, s, n, fe, pa, pr, t);
        this.sprintTasks = sprintTasks;
        this.activate = true;
    }

    public BacklogEntries(String f, String d, int s, int n, EnumFirstEstimate fe, int pa, EnumPriority pr, int t, List<SprintTasks> sprintTasks, Sprint sprint, Projects projects) {
        this(f, d, s, n, fe, pa, pr, t, sprintTasks, sprint);
        this.projects = projects;
        this.activate = true;
    }

    public Boolean stateExist(EnumState state, Sprint sprint) {
        for (SprintTasks task : getTasksInSprintAndFeature(sprint)) {
            if (task.getState() == state) {
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public List<SprintTasks> getSprintTasks() {
        return sprintTasks;
    }

    public void setSprintTasks(List<SprintTasks> sprintTasks) {
        this.sprintTasks = sprintTasks;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getState() {
        return state;
    }

    public int getNumber() {
        return number;
    }

    public EnumFirstEstimate getFirstEstimate() {
        return firstEstimate;
    }

    public int getParentId() {
        return parentId;
    }

    public EnumPriority getPriority() {
        return priority;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFirstEstimate(EnumFirstEstimate firstEstimate) {
        this.firstEstimate = firstEstimate;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setPriority(EnumPriority priority) {
        this.priority = priority;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }


    public List<Sprint> getSprints() {
        return sprint;
    }


    public void setSprint(Sprint sprint) {
        if(this.sprint == null){
            this.sprint = new ArrayList<Sprint>();
        }
        this.sprint.add(sprint);
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public enum EnumPriority {
        VERYLOW(1, "Very Low"), LOW(2, "Low"), AVERAGE(3, "Average"),
        HIGH(4, "High"), VERYHIGH(5, "Very High");

        private int value;
        private String description;
        EnumPriority(int value, String description) {
            this.value = value;
            this.description = description;
        }
        public int getValue() {
            return value;
        }
        public String getDescription() {
            return description;
        }
        public static EnumPriority fromString(String priority){
            priority = priority.toUpperCase().replaceAll(" ", "");
            return EnumPriority.valueOf(priority);
        }
    }

    public enum EnumFirstEstimate {
        VERYSHORT(1, "Very short duration [-2h]"),
        SHORT(2, "Short duration [2~4h]"),
        SHORTERTHANAVERAGE(3, "Shorter than average duration [4~8h]"),
        LONGERTHANAVERAGE(4, "Longer than average duration [8~16h]"),
        LONG(5, "Long duration [16~40h]"),
        VERYLONG(6, "Very long duration [+40h]");

        private int value;
        private String description;
        EnumFirstEstimate(int value, String description) {
            this.value = value;
            this.description = description;
        }
        public int getValue() {
            return value;
        }
        public String getDescription() {
            return description;
        }
    }

    public String getFirstEstimateFormat(){
        if (this.firstEstimate == null) {
            return "No duration";
        } else {
            return this.firstEstimate.getDescription();
        }
    }
    public String getPriorityFormat(){
        if (this.priority == null) {
            return "No priority";
        } else {
            return this.priority.getDescription();
        }
    }


    public Boolean workingAlready() {
        Boolean found = false;
        for (SprintTasks sprintTasks : getSprintTasks().stream().filter(sprintTasks1 -> sprintTasks1.getState() == EnumState.DOING).collect(Collectors.toList())) {
            found = sprintTasks.getMembersTaskDoings()
                    .stream()
                    .anyMatch(membersTasks -> membersTasks.getMembers().getId() == SessionController.getMemberId());
            if (found)
                return found;
        }
        for (SprintTasks sprintTasks : getSprintTasks().stream().filter(sprintTasks1 -> sprintTasks1.getState() == EnumState.TOREVIEW).collect(Collectors.toList())) {
            found = sprintTasks.getMembersTasksReviews()
                    .stream()
                    .anyMatch(membersTasks -> membersTasks.getMembers().getId() == SessionController.getMemberId());
            if (found)
                return found;
        }
        return found;
    }

    public List<SprintTasks> getTasksInSprintAndFeature(Sprint sprint){
        List<SprintTasks> tasks = new ArrayList<SprintTasks>();
        for(SprintTasks task : sprintTasks){
            if(task.getSprint() != null){
                if(task.getSprint().getId() == sprint.getId()){
                    tasks.add(task);
                }
            }
        }
        Collections.sort(tasks, (o1, o2) -> o1.getId() - o2.getId());
        return tasks;
    }

    public static Finder<Integer, BacklogEntries> find = new Finder<Integer, BacklogEntries>(BacklogEntries.class);

}

