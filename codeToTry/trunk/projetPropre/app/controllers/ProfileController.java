package controllers;

import java.util.List;
import javax.inject.Inject;
import models.*;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.FormFactory;

public class ProfileController extends Controller{
	
	@Inject
	private FormFactory formFactory;
	
	public Result profile(String cip) {
		Users user = Users.find.byId(cip);
		
		Result page = null;
		if(session("project") == null) {
			session("project","P0");
		}
		String resume = user.getResume(session("language"));
		String status = user.getStatus(session("language"));
		
		if(session("user") != null) {
			
			switch(session("user")) {
				case "admin": 	page = ok(views.html.templates.TemplateAdmin.render("Profile : "+user.getCip(),views.html.profils.ProfileManagement.render(user, resume,status)));
								break;
			
				case "owner":	if(Administrator.getAdministratorByCIP(cip)==null) {
									page = ok(views.html.templates.TemplateOwner.render("Profile : "+user.getCip(),views.html.profils.ProfileManagement.render(user, resume,status)));
								}else {
									page = ok(views.html.templates.TemplateOwner.render("Profile : "+user.getCip(),views.html.profils.Profile.render(user, resume,status)));
								}				
								break;
				
				case "user": 	if(session("name")!=null && session("name").equals(cip)) {
									page = ok(views.html.templates.TemplateUser.render("Profile : "+user.getCip(),views.html.profils.ProfileManagement.render(user, resume,status)));
								}else {
									page = ok(views.html.templates.TemplateUser.render("Profile : "+user.getCip(),views.html.profils.Profile.render(user, resume,status)));
								}
								break;
					
				default : 		page = ok(views.html.templates.Template.render("Profile : "+user.getCip(),views.html.profils.Profile.render(user, resume,status)));
								break;
			
			}
		} else {
			page = ok(views.html.templates.Template.render("Profile : "+user.getCip(),views.html.profils.Profile.render(user,resume,status)));
		}
		return page;
	}
	
	public Result editProfile(String cip) {
		Users user = Users.find.byId(cip);
		Result page = null;
		List<String> status = Status.listStatusDependOfUser(user);
		if(session("project") == null) {
			session("project","P0");
		}
		if(session("user") != null) {
			
			switch(session("user")) {
			case "admin": 	page = ok(views.html.templates.TemplateAdmin.render("Profile : "+user.getCip(),views.html.profils.EditProfileAdmin.render(user, cip,status)));
							break;
		
			case "owner":	if(session("name") != null && session("name").equals(cip)) {
								page = ok(views.html.templates.TemplateOwner.render("Profile : "+user.getCip(),views.html.profils.EditMyProfileOwner.render(user, cip,status)));
							}else if(Administrator.getAdministratorByCIP(cip)==null) {
								page = ok(views.html.templates.TemplateOwner.render("Profile : "+user.getCip(),views.html.profils.EditProfileOwner.render(user, cip,status)));
							}else {
								flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
								page = redirect(routes.ProfileController.profile(cip));
							}
							break;
			
			case "user": 	if(session("name") != null && session("name").equals(cip)) {
								page = ok(views.html.templates.TemplateUser.render("Profile : "+user.getCip(),views.html.profils.EditMyProfile.render(user, cip)));
							}else {
								flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
								page = redirect(routes.ProfileController.profile(cip));
							}	
							break;
				
			default : 		flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
							page = redirect(routes.ProfileController.profile(cip));
							break;
			}
		}else {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
			page = redirect(routes.ProfileController.profile(cip));
		}
		return page;
	}
	
	public Result editProfileFormValidation(String id) {
		Users user = Users.find.byId(id);
		DynamicForm boundForm  = formFactory.form().bindFromRequest();
		if(session("project") == null) {
			session("project","P0");
		}
		String password = boundForm.get("password");
		String verifPassword = boundForm.get("verifPassword");
		Result page = null;
		
		if (boundForm.hasErrors()) {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			page = redirect(routes.ProfileController.editProfile(id));
		} else if(boundForm.get("send").equals("send")){
			user.setFirstName(boundForm.get("firstname"));
			user.setLastName(boundForm.get("lastname"));
			user.setMail(boundForm.get("mail"));
			user.setResumeEn(boundForm.get("resumeEn"));
			user.setResumeFr(boundForm.get("resumeFr"));
			user.setStatus(Status.find.byId(boundForm.get("status")));
			if((password!=null && verifPassword!=null) && !password.equals("")) {
				if(password.equals(verifPassword)) {
					user.setPassword(password);
					user.save();
					flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MODIFICATION_OK));
					page = redirect(routes.ProfileController.profile(id));
				}else {
					flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MAILBOXWEBSITEPASSWORD));
					page = redirect(routes.ProfileController.editProfile(id));
				}
			}else {
				user.save();
				flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MODIFICATION_OK));
				page = redirect(routes.ProfileController.profile(id));
			}
		}else {
			flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITHOME_CANCEL));
			page = redirect(routes.ProfileController.profile(id));
		}	
		return page;
	}
	
	public Result editMyProfileFormValidation(String id) {
		
		Users user = Users.find.byId(id);
		DynamicForm boundForm  = formFactory.form().bindFromRequest();
		if(session("project") == null) {
			session("project","P0");
		}
		String password = boundForm.get("password");
		String verifPassword = boundForm.get("verifPassword");
		Result page = null;
		if (boundForm.hasErrors()) {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			page = redirect(routes.ProfileController.editProfile(id));	
		}else if(boundForm.get("send").equals("send")) {
			
			user.setFirstName(boundForm.get("firstname"));
			user.setLastName(boundForm.get("lastname"));
			user.setMail(boundForm.get("mail"));
			user.setResumeEn(boundForm.get("resumeEn"));
			user.setResumeFr(boundForm.get("resumeFr"));
			
			if((password!=null && verifPassword!=null) && !password.equals("") ){
				if(password.equals(verifPassword)) {
					user.setPassword(password);
					user.save();
					flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MODIFICATION_OK));
					page = redirect(routes.ProfileController.profile(id));
				}else {
					flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MAILBOXWEBSITEPASSWORD));
					page = redirect(routes.ProfileController.editProfile(id));
				}
			}else {
				user.save();
				flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MODIFICATION_OK));
				page = redirect(routes.ProfileController.profile(id));
			}
		}else {
			flash(ConstanteMessagesFlash.SUCCESS,ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITHOME_CANCEL));
			page = redirect(routes.ProfileController.profile(id));
		}
		return page;
	}	


}
	
