package controllers;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;

import play.mvc.Controller;
import play.mvc.Result;



import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginController extends Controller {
	
	private final String ERROR = "error";
	private final String SUCCESS = "success";
	private final String ERROR_MESSAGE = "Sorry but your login or password are incorrect, please try again";
	private final String CONNECT_MESSAGE = "Connected";
	private final String DISCONNECT_MESSAGE = "Disconnected";
	
	@Inject
	private FormFactory formFactory;

	public Result formulaire() {
		if(session("name") != null) {
			
			return redirect(routes.HomeController.home());
			
		}else {
			
			return ok(views.html.Login.render("Login"));

		}
	}

	public Result valider() {
		
		DynamicForm boundForm  = formFactory.form().bindFromRequest();
		String pseudo = boundForm.get("pseudo");
		String password = boundForm.get("password");
		
		if (boundForm.hasErrors()) {
			
			flash("error",ERROR_MESSAGE);
			return redirect(routes.LoginController.formulaire());

		} else {

			if (session("name") == null) {

				if (Users.find.byId(pseudo) != null && password.contentEquals(Users.find.byId(pseudo).getPassword())) {
					
					session("name", pseudo);
					session("mdp", password);
					flash(SUCCESS,CONNECT_MESSAGE);
					return redirect(routes.HomeController.home());

				} else {
					
					flash(ERROR,ERROR_MESSAGE);
					return redirect(routes.LoginController.formulaire());

				}

			} else {

				return redirect(routes.HomeController.home());

			}

		}

	}

	public Result deconnection() {

		session().remove("name");
		session().remove("mdp");
		flash(SUCCESS,DISCONNECT_MESSAGE);
		return redirect(routes.LoginController.formulaire());

	}

}