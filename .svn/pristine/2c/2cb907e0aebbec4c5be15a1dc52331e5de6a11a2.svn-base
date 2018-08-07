package models;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class MembersTasksDoing extends MembersTasks implements interfaces.MembersTasks {

    public MembersTasksDoing(int t, Date d) {
        super(t, d);
    }

    public MembersTasksDoing(int t, Date d, SprintTasks st, Members m) {
        super(t, d, st, m);
    }

    @Override
    public int getOthersTimeSpent() {
        return this.sprintTasks.getWorkingTime() - this.getTimeSpent();
    }
    public static Finder<Integer, MembersTasksDoing> find = new Finder<>(MembersTasksDoing.class);
}