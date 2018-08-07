package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
public class Project extends Model {

	public static final Finder<String, Project> find = new Finder<String, Project>(Project.class);

	@Id
	private String name;

	@ManyToMany(cascade=CascadeType.ALL)
	private List<Owner> owners = new ArrayList<Owner>();

	@Lob
	private String resumeFr;
	
	@Lob
	private String resumeEn;
	
	/*//tentative d'ajout de photo
	@Lob
	private String picture;
	*/
	public Project(String name, String resumeFr,String resumEn,Owner owner) {
		this.name = name;
		this.resumeFr=resumeFr;
		this.resumeEn=resumEn;
		this.owners.add(owner);
	}
	public Project(String name, Owner owner) {
		this.name=name;
		this.owners.add(owner);
	}
	
	public void removeOwner(Owner owner) {
		owners.remove(owner);
	}
	public String getName() {
		return name;
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public String getResumeFr() {
		
		return resumeFr;
		
	}
	
	public void setResumeFr(String resume) {
		
		this.resumeFr=resume;
		
		
	}
	
	public String getResumeEn() {
		
		return resumeEn;
		
	}
	
	public void setResumeEn(String resume) {	
		this.resumeEn=resume;	
	}
	public boolean equals(Project project) {
		
		return this.getName().equals(project.getName());
		
	}

	public static boolean find_project_owner(Owner owner) {
		List<Project> listProject = Project.find.all();
		List<Owner> listOwner;
		boolean ret = false;
		for(int i=0;i<listProject.size();i++) {
			listOwner=listProject.get(i).getOwners();
			for(int j=0; j<listOwner.size();j++) {                                                                                                                                                                                                                               
				if(listOwner.get(j).getuserOwner().getCip().equals(owner.getuserOwner().getCip())) {
					ret=true;
				}
			}
			
		}
		return ret;
	}
	
}