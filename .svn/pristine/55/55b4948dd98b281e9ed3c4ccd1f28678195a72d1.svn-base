package factories;


import models.BacklogEntries;
import models.Sprint;

import java.util.Date;

public class SprintFactory {
    public static Sprint createNewSprintWithNtasks ( int numberOfTasks ) {
        return createNewSprintWithNtasksInFeatures(1,numberOfTasks);
    }

    public static Sprint createNewSprintWithNtasksInFeatures(int numberOfFeatures, int numberOfTasksByFeature ) {
        Sprint sprintToReturn = new Sprint("",new Date(), new Date());
        for (int intLoop = 0; intLoop < numberOfFeatures ; intLoop ++) {
            BacklogEntries backlogEntries = BacklogEntriesFactory.createFeatureWithNnewTasks(numberOfTasksByFeature);
            backlogEntries.setSprint(sprintToReturn);
            sprintToReturn.addBacklogEntrie(backlogEntries);
        }
        return sprintToReturn;
    }

    public static Sprint createNewSprintWithNfeaturesWithAllOfTasksType ( int numberOfFeatures ) {
        Sprint sprintToReturn = new Sprint("",new Date(), new Date());
        for (int intLoop = 0; intLoop < numberOfFeatures ; intLoop ++) {
            BacklogEntries backlogEntries = BacklogEntriesFactory.createFeatureWithOneOfAllTasks();
            backlogEntries.setSprint(sprintToReturn);
            sprintToReturn.addBacklogEntrie(backlogEntries);
        }
        return sprintToReturn;
    }
}
