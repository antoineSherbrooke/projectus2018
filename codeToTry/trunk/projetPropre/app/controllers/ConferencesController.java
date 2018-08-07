package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ConferencesController extends Controller
{
	public Result conferences()
	{
		Result page;
		
		if(session("user")!=null) {
			switch(session("user")) {
			case "admin": page = ok(views.html.templates.TemplateAdmin.render("Team",views.html.Conferences.render()));
			break;
			case "owner": page = ok(views.html.templates.TemplateOwner.render("Team",views.html.Conferences.render()));
			break;
			case "user" :page= ok(views.html.templates.TemplateUser.render("Team",views.html.Conferences.render()));;
			break;
			default:page =ok(views.html.templates.Template.render("Team",views.html.Conferences.render()));;
			
			}
			
		}else {
			page =ok(views.html.templates.Template.render("Team",views.html.Conferences.render()));
		}
		return page;
	}
}