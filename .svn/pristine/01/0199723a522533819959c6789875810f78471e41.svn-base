package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import io.ebean.Finder;
import io.ebean.Model;

@Entity
public class Owner extends Model{

	public static final Finder<Integer, Owner> find = new Finder<Integer, Owner>(Owner.class);
	
	
	
	@Id
	private Integer idOwner;
	
	@ManyToOne(cascade=CascadeType.ALL)
  	private Users userOwner;
  
	@ManyToMany(cascade=CascadeType.ALL)
	private List<Project> projects = new ArrayList<Project>();
	
	public Owner(Users user) {
		userOwner = user;
		int max;
		if(Owner.find.all().size() !=0 ){
			max =Owner.find.all().get(Owner.find.all().size() - 1).getIdOwner();
			for(int i =0 ; i<Owner.find.all().size();i++) {
				if(Owner.find.all().get(i).getIdOwner()>max) {
					max =Owner.find.all().get(i).getIdOwner();
				}
			}
			this.idOwner = max+ 1;
		}else {
			idOwner = 0;
		}
		
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
	}
	public void removeProject(Project project) {
		this.projects.remove(project);
	}
	public Users getuserOwner() {
		return userOwner;
	}

	public Integer getIdOwner() {
		return this.idOwner;
	}
	public List<Project> getOwners() {
		return projects;
	}
	
	public static Owner getOwnerByCIP(String cip) {
		Owner result = null;
		List<Owner> listowner= Owner.find.all();
		for(int i=0; i<listowner.size();i++) {
			if(listowner.get(i).getuserOwner().getCip().equals(cip)) {
				result=listowner.get(i);
			}
		}
		return result;
	}
	public boolean equals(Owner owner) {
		return this.getuserOwner().equals(owner.getuserOwner());
	}
}
