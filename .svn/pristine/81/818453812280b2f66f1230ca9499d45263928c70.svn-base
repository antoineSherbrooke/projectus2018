package models;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import static java.util.Collections.reverse;


@Entity
public class Projects extends Model{
	
	@Id
	private Integer id;
	
	@Required
	private String description;
	private String name;
	private Boolean state;

	@OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private Groups groups;
	
	@OneToMany
	private List<Releases> releases;
	
	@OneToMany
	private List<Meetings> meetings;

    @ManyToMany
    private List<Supervisors> supervisors;

	@OneToMany
	private List<BacklogEntries> backlogEntries;

	@OneToMany
	private List<History> histories;

	public Projects(String description, String name, Boolean state){
    	this.description = description;
    	this.name = name;
    	this.state = state;
    
    }
    public Projects(String description, String name, Boolean state,List<Releases> releases,List<Meetings> meetings, Groups groups){
    	this.description = description;
    	this.name = name;
    	this.state = state;
    	this.releases = releases;
    	this.meetings = meetings;
    	this.groups = groups;
    }

    public Projects(String description, String name, Boolean state,List<Releases> releases,List<Meetings> meetings, Groups groups,List<BacklogEntries> backlogEntries){
    	this.description = description;
    	this.name = name;
    	this.state = state;
    	this.releases = releases;
    	this.meetings = meetings;
    	this.groups = groups;
    	this.backlogEntries = backlogEntries;
    }

	public Projects() {
		this("", "dummy project", false);
	}

	public Integer getId() {
		return id;
	}

	public List<Supervisors> getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(List<Supervisors> supervisors) {
		this.supervisors = supervisors;
	}

	public List<Releases> getReleases() {
		return releases;
	}

	public void setReleases(List<Releases> releases) {
		this.releases = releases;
	}
	
	public void setMeetings(List<Meetings> meetings){
	    this.meetings = meetings;
	}

	public List<Meetings> getMeetings() {
		return meetings;
	}
	
	public Groups getGroups(){
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public String getDescription(){
	    return description;
	}
	public String getName(){
	    return name;
	}
	public Boolean getState(){
	    return state;
	}
	public List<History> getHistory() { return histories; }

	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
    	this.description = description;
	}
	public void setState(Boolean state){
    	this.state = state;
	}
	public List<BacklogEntries> getBacklogEntries(){
	    return backlogEntries;
	}
	public void setBacklogEntries(List<BacklogEntries> backlogEntries){
	    this.backlogEntries = backlogEntries;
	}
	public void setHistory(List<History> historys) { this.histories = historys; }

	public List<History> getNotificationHistory(){
		List<History> histories = getHistory();
        Collections.reverse(histories);
		return histories.stream().limit(8).collect(Collectors.toList());
	}

    public String getListSupervisorsToString(){
        String supervisors = "";
        for(Supervisors supervisor:getSupervisors()){
            supervisors = supervisors+", "+supervisor.getAssociations().getUsers().getFirstName()+" "+supervisor.getAssociations().getUsers().getLastName();
        }
        if(supervisors.equals("")){
			supervisors = "  No supervisor";
        }
		supervisors = supervisors + ".";
        return supervisors.substring(2);
    }

	public static Finder<Integer, Projects> find = new Finder<Integer,Projects>(Projects.class);
	
}