package controllers;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.index;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class HomeController extends Controller {

    public Result index() {

        if(SessionController.connected()){
    		return redirect(routes.DashboardController.dashboard().url());
    	}
        return ok(index.render());
    }

    public Result about() {
        return ok(about.render());
    }
 
    public Result switchNav(String nav){
    	session("nav", nav);
    	return ok("ok");
    }

    public static String version() throws IOException {
        if (!Play.isTest()) {
            Assets.Asset asset = new controllers.Assets.Asset("version.txt");
            URL website = new URL(routes.Assets.versioned(asset).absoluteURL(request()));
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
            return response.toString();
        } else {
            return "test build";
        }
    }


    
}

