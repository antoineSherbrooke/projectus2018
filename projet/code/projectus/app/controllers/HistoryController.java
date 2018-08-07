package controllers;

import com.avaje.ebean.Ebean;
import models.History;
import models.Members;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.history;

import java.util.List;


public class HistoryController extends Controller {
    public Result index() {
        List<History> histories = History.find.where().eq("projects_id",
                SessionController.getProjectId()).orderBy("date desc").findList();
        return ok(history.render(histories));
    }
}

