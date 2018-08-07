package models;

import javax.persistence.*;

import controllers.ConstanteMessages;

import java.util.ArrayList;
import java.util.List;
import io.ebean.*;
import io.ebean.annotation.NotNull;


@Entity
public class Users extends Model {

	public static final Finder<String, Users> find = new Finder<String, Users>(Users.class);

	@Id
	private String cip;

	@NotNull
	private String password;

	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@Column( unique = true, nullable = false)
	private String mail;
	@Lob
	private String resumeFr;
	@Lob
	private String resumeEn;
	
	@ManyToOne
  	private Status userStatus;
	
	@ManyToMany(cascade=CascadeType.ALL)
	List<ProjectUserParticipation> listProjectParticipation = new ArrayList<ProjectUserParticipation>();
	
	public Users(String cip, String password, String firstName, String lastName, String mail,Status userStatus) {

		this.cip = cip;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.userStatus=userStatus;

	}
	
	
	public static String getCipByMail(String mail) {
		List<Users> users = Users.find.all();
		String cip = null; 
		for(Users u : users) {
			
			if(u.mail != null && u.mail.equals(mail)) {
				
				cip = u.cip;
				
			}
		}
		return cip;
	}
	
	public static boolean mailPresents(String mail) {

		List<Users> users = Users.find.all();
		boolean res = false;

		for (Users u : users) {

			if (u.mail != null && u.mail.equals(mail)) {

				res = true;

			}

		}

		return res;
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
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCip() {
		return this.cip;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setStatus(Status userStatus) {
		this.userStatus = userStatus;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getMail() {
		return this.mail;
	}
	
	public Status getStatus() {
		return userStatus;
	}
	
	public String getStatusText() {
		return userStatus.getName();
	}
	
	public boolean equals(Users user) {
		return this.cip.equals(user.getCip());
	}
	
	public List<Project> getProjects() {
			return ProjectUserParticipation.getUserProjects(this);
	}

	public String getResume(String session) {
		String resume;
		if(session != null && session.equals("french")) {
			if(this.getResumeFr() != null) {
				resume= this.getResumeFr();
			}else {
				resume=  ConstanteMessages.NO_RESUME_FR;
			}
			
		}else {
			
			if(this.getResumeEn() != null) {
				resume = this.getResumeEn();
			}else {
				resume =  ConstanteMessages.NO_RESUME_EN;
			}
			
		}
		return resume;
	}
	
	public String getStatus( String session) {
		
		String status;
		if(session != null && session.equals("french")) {
			if(this.getStatusText() != null) {
				status = this.getStatusText();
			}else {
				status = ConstanteMessages.NO_STATUS_FR;
			}
			
		}else {
			
			if(this.getStatusText() != null) {
				status = this.getStatusText();
			}else {
				status = ConstanteMessages.NO_STATUS_EN;
			}
			
		}
		return status;
	}

}