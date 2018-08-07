package models;


import javax.persistence.*;
import com.avaje.ebean.Model;

import play.data.validation.Constraints;

@Entity
public class Associations extends Model{

	@Id
	private Integer id;

	@Constraints.Required
	private EnumAsso associationType;

	@ManyToOne
	private Users users;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Members members;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Supervisors supervisors;

	public Associations(Users user, EnumAsso associationType){
		this.users = user;
		this.associationType = associationType;
	}
	public Associations(Users user, EnumAsso associationType, Members members){
		this.users = user;
		this.associationType = associationType;
		this.members = members;
	}

	public Supervisors getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(Supervisors supervisors) {
		this.supervisors = supervisors;
	}

	public Members getMembers() {
		return members;
	}

	public void setMembers(Members members) {
		this.members = members;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Users getUsers() {
		return users;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EnumAsso getAssociationType() {
		return associationType;
	}

	public void setAssociationType(EnumAsso associationType) {
		this.associationType = associationType;
	}

	public enum EnumAsso {
        ADMINISTRATOR(1), SUPERVISOR(2), MEMBER(3);
        private int value;

        EnumAsso(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
	
	public static Finder<Integer, Associations> find = new Finder<Integer,Associations>(Associations.class);
}
