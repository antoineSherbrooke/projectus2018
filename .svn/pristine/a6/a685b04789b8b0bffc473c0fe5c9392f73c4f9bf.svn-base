package controllers;

import Security.Secure;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Projects;
import models.Releases;
import models.Sprint;
import utils.Dates;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.With;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@With(Secure.class)
public class WarningController extends Controller {
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm");
    private static final String warningTimesKey = "warning_times_list";
    private static final String indexNonWarnedKey = "warning_times_current_index";
    public static final Long[] defaultWarningTimes = {Dates.day, 12* Dates.hour, 2* Dates.hour, Dates.hour, 15*Dates.minute};
    public static long checkDelay = (long)(0.5*Dates.minute);


    public static String warnDeadlinesMessage() {
        String message = "";
        if (mustReadDates()) {
            Projects project = SessionController.getProjectRunning();
            if (project != null) {
                int projectId = project.getId();

                Releases releaseToCheck = Releases.find.where().and(
                        Expr.eq("projects_id", projectId),
                        Expr.and(Expr.eq("running", true), Expr.eq("active", true))).findUnique();
                if (releaseToCheck != null) {
                    Date now = new Date();

                    Sprint sprintToCheck = Sprint.find.where().and(
                            Expr.eq("releases_id", releaseToCheck.getId()),
                            Expr.eq("state", Sprint.State.RUNNING)).findUnique();
                    if (sprintToCheck != null) {
                        message += warnDateMessage(now, sprintToCheck.getEndDate(),
                                "sprint \"" + sprintToCheck.getName() + "\"", "sprint");
                    }
                    message += warnDateMessage(now, releaseToCheck.getReleaseDate(),
                            "release \"" + releaseToCheck.getName() + "\"", "release");
                }
            }
        }
        return message;
    }

    public static boolean mustReadDates() {
        String dateKey = "last_date_check";
        Date now = new Date();
        Boolean mustCheck;

        if (session().containsKey(dateKey)) {
            try {
                long timestamp = Json.parse(session().get(dateKey)).asLong();
                mustCheck = (now.getTime() - timestamp > checkDelay);
            } catch (Exception e) {
                mustCheck = true;
            }
        } else {
            mustCheck = true;
        }
        if (mustCheck) {
            session().put(dateKey, String.valueOf(now.getTime()));
        }
        return mustCheck;
    }


    private static String warnDateMessage(Date now, Date endDate, String nameItem, String typeItem) {
        long diff_ms = endDate.getTime() - now.getTime();

        int currentProject = SessionController.getProjectNumberRunning();
        List<Long> times = getWarningTimes(currentProject, typeItem);

        int indexWarningTime = -1;
        for (int i = 0; i < times.size(); i++) {
            if (diff_ms > 0 && diff_ms < times.get(i)) {
                indexWarningTime = i;
            }
        }
        if (indexWarningTime >= 0) {
            setIndexNonWarned(indexWarningTime+1, currentProject, typeItem);

            String timeLeft = Dates.millisecondsToString(endDate.getTime() - now.getTime(), Dates.Unit.HOURS);

            String message = String.format("The %s will end in %s. It will be automatically concluded at %s\n",
                    nameItem, timeLeft, dateFormat.format(endDate.getTime()));
            return message;
        } else {
            return "";
        }
    }

    private static List<Long> getWarningTimes(int projectId, String typeItem) {
        Boolean resetList = false;
        List<Long> nonWarnedTimes = new ArrayList<>();

        if (session().containsKey(warningTimesKey) && session().containsKey(indexNonWarnedKey)) {
            try {
                nonWarnedTimes = readNonWarnedTimes(projectId, typeItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resetList = true;
        }
        if (resetList) {
            resetWarningTimes();
            nonWarnedTimes = new ArrayList<>(Arrays.asList(defaultWarningTimes));
        }
        return nonWarnedTimes;
    }

    private static void setIndexNonWarned(int indexToSet, int projectId, String typeItem) {
        JsonNode jsource = Json.parse(session(indexNonWarnedKey));
        ArrayNode osource = Json.fromJson(jsource, ArrayNode.class);
        ArrayNode onew = new ArrayNode(JsonNodeFactory.instance);

        for (int i = 0; i < osource.size(); i++) {
            ObjectNode toAdd = Json.fromJson(osource.get(i), ObjectNode.class);
            if (i == projectId) {
                toAdd.put(typeItem, indexToSet);
            }
            onew.add(toAdd);
        }
        session().put(indexNonWarnedKey, Json.toJson(onew).toString());
    }

    private static List<Long> readNonWarnedTimes(int projectId, String typeItem) throws RuntimeException {
        List<Long> nonWarnedTimes = new ArrayList<>();
        JsonNode jtimes = Json.parse(session(warningTimesKey));

        int indexNonWarned = Json.parse(session().get(indexNonWarnedKey)).get(projectId).get(typeItem).asInt();

        for (int i = indexNonWarned; i < jtimes.size(); i++) {
            nonWarnedTimes.add(jtimes.get(i).asLong());
        }
        return nonWarnedTimes;
    }

    public static void resetWarningTimes() {
        int numBerOfProjects = Json.parse(session().get("project")).size();
        ArrayNode indexNonWarned = new ArrayNode(JsonNodeFactory.instance);
        for (int i = 0; i < numBerOfProjects; i++) {
            ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
            objectNode.put("release", 0);
            objectNode.put("sprint", 0);
            indexNonWarned.add(objectNode);
        }
        session().put(warningTimesKey, Json.toJson(defaultWarningTimes).toString());
        session().put(indexNonWarnedKey, Json.toJson(indexNonWarned).toString());
    }

}