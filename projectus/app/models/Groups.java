package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

@Entity
public class Groups extends Model{
	
	@Id
	private Integer id;
	
	@Required
	private String name;

	@Constraints.Email
	private String email;

	@OneToOne(cascade= CascadeType.ALL,fetch = FetchType.EAGER)
	private Projects projects;
	
	@OneToMany
	private List<Members> members;
	
	public Groups(Projects projects, String name){
		this.projects = projects;
		this.name = name;
	}
	
	public Groups(String name, String email){
	    this.name = name;
	    this.email = email;
	}
	public Groups(String name, String email,Projects p){
	    this.name = name;
	    this.email = email;
	    this.projects = p;
	}

	public Groups(String name, String email, Projects projects, List<Members> members) {
		this.name = name;
		this.email = email;
		this.projects = projects;
		this.members = members;
	}

	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getEmail() {
		return email == null ? "" : email;
	}


	public Projects getProjects() {
		return projects;
	}


	public List<Members> getMembers() {
		return members;
	}
	
	public void setMembers(List<Members> m){
	    this.members = m;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	public List<Users> getUsers(){
		List<Users> users = new ArrayList<>();
		for(Members member : members){
			if(!users.contains(member.getAssociations().getUsers())){
                users.add(member.getAssociations().getUsers());
            }
		}
        Collections.sort(users, (o1, o2) -> o1.getCip().compareTo(o2.getCip()));
        return users;
	}
	


	public static Finder<Integer, Groups> find = new Finder<Integer,Groups>(Groups.class);
	
	
	
	
}
