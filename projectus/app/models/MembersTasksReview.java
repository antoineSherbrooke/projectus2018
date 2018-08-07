package models;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class MembersTasksReview extends MembersTasks implements interfaces.MembersTasks {

    public MembersTasksReview(int t, Date d) {
        super(t, d);
    }

    public MembersTasksReview(int t, Date d, SprintTasks st, Members m) {
        super(t, d, st, m);
    }

    @Override
    public int getOthersTimeSpent() {
        return this.sprintTasks.getWorkingTimeReview() - this.getTimeSpent();
    }

    public static Finder<Integer, MembersTasksReview> find = new Finder<>(MembersTasksReview.class);
}