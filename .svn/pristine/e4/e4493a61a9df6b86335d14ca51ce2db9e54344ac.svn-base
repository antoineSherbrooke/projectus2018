package models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;
import io.ebean.*;
import play.data.validation.*;

@Entity
public class LostPassword extends Model {
	public static final Integer TIMELAPSE=5;
	public static final Finder<String, LostPassword> find = new Finder<String, LostPassword>(LostPassword.class);

	@Id
	private String token;

	@Constraints.Required
	private String pseudo;

	@Constraints.Required
	private Calendar dateOfLapse;

	public LostPassword(String token, String pseudo) {

		this.token = token;
		this.pseudo = pseudo;
		this.dateOfLapse = Calendar.getInstance();
		this.dateOfLapse.set(Calendar.MINUTE, dateOfLapse.get(Calendar.MINUTE) + TIMELAPSE);

	}
	public LostPassword(String token, String pseudo, Calendar date) {
		this.token = token;
		this.pseudo = pseudo;
		this.dateOfLapse = date;
	}
	
	public static LostPassword findById(String id) {
		List<LostPassword> usersList = LostPassword.find.all();
		LostPassword res = null;
		for(LostPassword l : usersList ) {
			if(l.token.equals(id)) {
				res = l; 
			}
		}
		return res;
	}
	
	public Calendar getDateOfLapse() {
		return this.dateOfLapse;
	}
	
	public String getPseudo() {
		return this.pseudo;
	}
	
	public String getToken() {
		return this.token;
	}
}
