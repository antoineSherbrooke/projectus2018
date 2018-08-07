package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Users;
import models.Administrator;
import models.Owner;
import models.Status;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class RegisterController  extends Controller{
	
	@Inject
	private FormFactory formFactory;
	
	
	public Result registerForm() {	
		if(session("project") == null) {
			session("project","P0");
		}
		if(session("name")!=null && Administrator.getAdministratorByCIP(session("name"))!= null) {
			
			List<Status> listStatus = Status.find.all();
			
			
			return ok(views.html.templates.TemplateAdmin.render("Register",views.html.Register.render(listStatus)));
			
		}else {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN);
			return redirect(routes.HomeController.home());
		}

	}
	
	public Result registerFormValidation() {
		
		DynamicForm boundForm  = formFactory.form().bindFromRequest();
		if(session("project") == null) {
			session("project","P0");
		}
		String cip = boundForm.get("cip");
		String firstName = boundForm.get("firstName");
		String lastName = boundForm.get("lastName");
		String mail = boundForm.get("mail");
		String password = boundForm.get("password");
		String status = boundForm.get("status");
		String role = boundForm.get("role");
		
		Users newUser;
		
		if (boundForm.hasErrors()) {
			
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			return redirect(routes.LoginController.loginForm());

		} else {

			if (Users.find.byId(cip) == null ) {
		
				newUser = new Users(cip,password,firstName,lastName,mail, Status.find.byId(status));
				newUser.save();
				this.registerRole(role, newUser);
				
				flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_OK));
				return redirect(routes.HomeController.home());

			} else {
				
				flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_NOT_OK));
				return redirect(routes.RegisterController.registerForm());

			}

		}

	}
	private void registerRole(String role,Users newUser) {
		Administrator admin;
		Owner owner;
		
		switch(role) {
		
			case "administrator":admin=new Administrator(newUser); admin.save();
			break;
			
			case "owner":owner=new Owner(newUser); owner.save();;	
			break;
			
			default:;
			break;
		
		}
	}
	
}
