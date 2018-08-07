package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class MeetingMembers extends Model {

    @Id
    private Integer id;

    @Constraints.Required
    private String memberCIP;
    private boolean active;

    @ManyToOne(cascade = CascadeType.ALL)
    private Meetings meetings;

    public MeetingMembers(String memberCIP, boolean active) {
        this.memberCIP = memberCIP;
        this.active = active;
    }

    public MeetingMembers(String memberCIP, boolean active, Meetings meetings) {
        this(memberCIP, active);
        this.meetings = meetings;
    }

    public String getName(){
        Users user = Users.find.where().eq("cip",memberCIP).findUnique();
        return user.getFullName();
    }

    public static Finder<Integer, MeetingMembers> find = new Finder<Integer,MeetingMembers>(MeetingMembers.class);

    public Integer getId() {
        return id;
    }

    public String getMemberCIP() {
        return memberCIP;
    }

    public void setMemberCIP(String memberCIP) {
        this.memberCIP = memberCIP;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Meetings getMeetings() {
        return meetings;
    }

    public void setMeetings(Meetings meetings) {
        this.meetings = meetings;
    }
}
