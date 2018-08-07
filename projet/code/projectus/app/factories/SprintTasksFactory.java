package factories;


import models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SprintTasksFactory {
    public static SprintTasks createNewEmptyTasks() {
        return (new SprintTasks("Name", SprintTasks.EnumState.TODO,new BacklogEntries("Name","Desc"),30));
    }

    public static void addAtasksInNewSprintInNewFeature( List<SprintTasks> tasksList){
        addAtasksInSprintInNewFeature(new Sprint("Name",new Date(), new Date()),tasksList);
    }

    public static void addAtasksInSprintInNewFeature(Sprint sprint, List<SprintTasks> tasksList){
        addAtasksInSprintInFeature(sprint, new BacklogEntries("Name","Desc"),tasksList);
    }

    public static void addAtasksInSprintInFeature(Sprint sprint, BacklogEntries feature, List<SprintTasks> tasksList){
        feature.setSprintTasks(tasksList);
        feature.setSprint(sprint);
        sprint.addBacklogEntrie(feature);
        for (SprintTasks taskLoop : tasksList) {
            taskLoop.setSprint(sprint);
        }
    }

    public static List<SprintTasks> createNnewSprintTasksInNewFeature (int numberOfTasks) {
        return createNnewSprintTasksInFeature (numberOfTasks, new BacklogEntries("Name","Description"));
    }

    public static List<SprintTasks> createNnewSprintTasksInFeature (int numberOfTasks, BacklogEntries feature) {
        List<SprintTasks> tasksList = new ArrayList<>();
        for (int intLoop = 0 ; intLoop  < numberOfTasks ; intLoop ++) {
            SprintTasks sprintTasks = new SprintTasks("Name", SprintTasks.EnumState.TODO,feature,30);
            tasksList.add(sprintTasks);
        }
        return tasksList;
    }
}
