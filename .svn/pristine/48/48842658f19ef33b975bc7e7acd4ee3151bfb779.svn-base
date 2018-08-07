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

	@Inject
	private FormFactory formFactory;

	public Result loginForm() {

		if(session("project") == null) {
			session("project","P0");
		}
		if(session("name") != null) {
			
			return redirect(routes.HomeController.home());
			
		}else {
			
			return ok(views.html.templates.Template.render("Login",views.html.Login.render()));

		}
	}

	public Result loginFormValidation() {
		if(session("project") == null) {
			session("project","P0");
		}
		DynamicForm boundForm  = formFactory.form().bindFromRequest();
		String cip = boundForm.get("cip");
		String password = boundForm.get("password");
		
		if (boundForm.hasErrors()) {
			
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			return redirect(routes.LoginController.loginForm());

		} else {

			if (session("name") == null) {

				if (Users.find.byId(cip) != null && password.contentEquals(Users.find.byId(cip).getPassword())) {
					
					if(Administrator.getAdministratorByCIP(cip)!=null) {
						session("user","admin");
					} else if(Owner.getOwnerByCIP(cip)!=null) {
						session("user","owner");
					} else {
						session("user","user");
					}
					
					session("name", cip);
					flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.CONNECT_MESSAGE));
					return redirect(routes.HomeController.home());

				} else {
					flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_CONNECTION_MESSAGE));
					return redirect(routes.LoginController.loginForm());
				}
			} else {
				return redirect(routes.HomeController.home());
			}
		}
	}

	public Result disconnection() {
		if(session("project") == null) {
			session("project","P0");
		}
		session().remove("name");
		session().remove("user");
		flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.DISCONNECT_MESSAGE));
		return redirect(routes.LoginController.loginForm());

	}

}