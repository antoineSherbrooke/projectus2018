package controllers;


import play.mvc.Controller;
import play.mvc.Result;


public class PublicationsController extends Controller
{
	public Result publications()
	{
		Result page=null;
		if(session("project") == null) {
			session("project","P0");
		}
		if(session("user")!=null) {
			switch(session("user")) {
				case "admin": 	page= ok(views.html.templates.TemplateAdmin.render("Publications",views.html.Publications.render()));
								break;
			
				case "owner":	page= ok(views.html.templates.TemplateOwner.render("Publications",views.html.Publications.render()));
								break;
				
				case "user": 	page= ok(views.html.templates.TemplateUser.render("Publications",views.html.Publications.render()));
								break;
					
				default : 		page= ok(views.html.templates.Template.render("Publications",views.html.Publications.render()));
								break;
			
			}
		} else {
			page= ok(views.html.templates.Template.render("Publications",views.html.Publications.render()));
		}
		return page;
	}
}