package models;


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.*;
import com.avaje.ebean.Model;

import play.data.validation.Constraints;

@Entity
public class Comments extends Model{

	@Id
	private Integer id;

	@Constraints.Required
	private String title;
	private String content;
	private Date date;

	@ManyToOne
	private SprintTasks sprintTasks;
	
	@ManyToOne
	private Members members;
	

	public Comments(String t, String c, Date d, SprintTasks s, Members m){
	    this.title = t;
	    this.content = c;
	    this.date = d;
	    this.sprintTasks = s;
	    this.members = m;
	}
	public Comments(String t, String c, Date d){
	    this.title = t;
	    this.content = c;
	    this.date = d;
	}
	

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	public SprintTasks getSprintTasks() {
		return sprintTasks;
	}

	public Members getMembers() {
		return members;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSprintTasks(SprintTasks task) {
		this.sprintTasks = task;
	}

	public void setMembers(Members members) {
		this.members = members;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDateStr() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateFormat.format(date);
	}
	
	public void setDate(Date d) {
		this.date = d;
	}



	public static Finder<Integer, Comments> find = new Finder<Integer,Comments>(Comments.class);
}
