package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class EditController extends Controller {
	
	public Result edit() {

		return ok(views.html.Edit.render("Edit"));
		
	}
	
}