package interfaces;

import models.Members;
import models.SprintTasks;

import java.util.Date;

public interface MembersTasks {
    int getId();
    void setId(int id);
    Date getDay();
    void setDay(Date d);
    int getTimeSpent();
    Members getMembers();
    void setMembers(Members members);
    void setTimeSpent(int timeSpent);
    SprintTasks getSprintTasks();
    void update();
    void setSprintTasks(SprintTasks sprintTasks);
    int getOthersTimeSpent();
}
