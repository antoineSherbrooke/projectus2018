package models;

import javax.persistence.*;
import java.util.List;
import io.ebean.*;
import play.data.validation.*;

@Entity
public class Users extends Model {

	public static final Finder<String, Users> find = new Finder<String, Users>(Users.class);

	@Id
	private String pseudo;

	@Constraints.Required
	private String password;

	private String firstName;
	private String lastName;
	private String mail;

	public Users(String pseudo, String password, String firstName, String lastName, String mail) {

		this.pseudo = pseudo;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;

	}
	
	
	public static String getPseudoByMail(String mail) {
		List<Users> users = Users.find.all();
		String pseudo = null; 
		for(Users u : users) {
			
			if(u.mail != null && u.mail.equals(mail)) {
				
				pseudo = u.pseudo;
				
			}
		}
		return pseudo;
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
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPseudo() {
		return this.pseudo;
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
}