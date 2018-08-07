package factories;


import models.BacklogEntries;
import models.SprintTasks;

import java.util.ArrayList;
import java.util.List;

public class BacklogEntriesFactory {
    public static BacklogEntries createFeatureWithOneOfAllTasks() {
        BacklogEntries feature = new BacklogEntries("Name","Description");
        SprintTasks sprintTasksTODO = new SprintTasks("Name", SprintTasks.EnumState.TODO,feature,30);
        SprintTasks sprintTasksDOING = new SprintTasks("Name", SprintTasks.EnumState.DOING,feature,30);
        SprintTasks sprintTasksTOREVIEW = new SprintTasks("Name", SprintTasks.EnumState.TOREVIEW,feature,30);
        SprintTasks sprintTasksDONE = new SprintTasks("Name", SprintTasks.EnumState.DONE,feature,30);
        List<SprintTasks> tasksList = new ArrayList<>();
        tasksList.add(sprintTasksTODO);
        tasksList.add(sprintTasksDOING);
        tasksList.add(sprintTasksTOREVIEW);
        tasksList.add(sprintTasksDONE);
        feature.setSprintTasks(tasksList);
        return feature;
    }

    public static BacklogEntries createFeatureWithNnewTasks ( int numberOfTasks ) {
        BacklogEntries feature = new BacklogEntries("Name","Description");
        feature.setSprintTasks(SprintTasksFactory.createNnewSprintTasksInFeature(numberOfTasks,feature));
        return feature;
    }
}
