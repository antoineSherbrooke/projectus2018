package models;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.text.DateFormat;
import java.util.stream.Collectors;

import com.avaje.ebean.Model;
import org.apache.commons.lang3.time.DateUtils;
import utils.Dates;

@Entity
public class Releases extends Model implements DateShiftable {

    private final static DateFormat format = new SimpleDateFormat("EEEE, d MMM, YYYY", Locale.ENGLISH);

    @Id
    private int id;

    private String name;
    private Date releaseDate;
    private String comment;
    private Boolean running;
    private Boolean active;

    @ManyToOne
    private Projects projects;

    @OneToMany
    private List<Sprint> sprints;

    public Releases() {
        this("dummy", new Date(), "", false, null);
    }
    public Releases(String name, Date releaseDate, String comment, Boolean running){
        this(name, releaseDate, comment, running, null);
    }
    public Releases(String name, Date releaseDate, String comment, Boolean running,Projects projects){
        this.name = name;
        this.setReleaseDate(releaseDate);
        this.comment = comment;
        this.running = running;
        this.projects = projects;
        this.active = true;
    }

    public Boolean hasActiveSprint() {
        Boolean hasActive = false;
        for (Sprint sprint : sprints) {
            if (sprint.isRunning()) {
                hasActive = true;
                break;
            }
        }
        return hasActive;
    }

    public List<Sprint> getSprintSorted() {
        List<Sprint> sprintsSorted = sprints;
        Collections.sort(sprintsSorted, (sprint1, sprint2) -> sprint1.getStartDate().compareTo(sprint2.getStartDate()));
        return sprintsSorted;
    }

    public String dateFormat(Date date){
        if(date == null){
            return "";
        }
        return format.format(date);
    }

    public static Releases checkRunning(List<Releases> releaseActive){
        Date now = new Date();
        List<Releases> releases = releaseActive.stream()
                .filter(releases1 -> releases1.getReleaseDate().after(now))
                .collect(Collectors.toList());

        Collections.sort(releases, (o1, o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));

        return releases.isEmpty() ? null : releases.get(0);
    }

    public Boolean  checkSprintRunning(){
        Boolean find = false;
        for(Sprint sprint : sprints){
            if(sprint.getState() != Sprint.State.INACTIVE){
                find = true;
            }
        }
        return find;
    }

    public Boolean isPast(){
        return getReleaseDate().before( new Date());
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = Dates.getEndOfDay(releaseDate);
    }

    public void setExactReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void shiftDatesBy(int milliseconds) {
        setExactReleaseDate(DateUtils.addMilliseconds(releaseDate, milliseconds));
    }

    public static Finder<Integer, Releases> find = new Finder<>(Releases.class);

    public Boolean getRunning() {
        return (running != null && running);
    }

    public List<Sprint> getSprint() {
        return sprints;
    }

    public void setSprint(List<Sprint> sprint) {
        this.sprints = sprint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
