package controllers;

import javax.inject.Inject;
import javax.inject.Singleton;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.DynamicForm;
import play.data.FormFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import play.data.Form;
import play.mvc.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Singleton
public class HomeController extends Controller {
	@Inject
	private FormFactory formFactory;

	public Result home() {
		if(session("project")==null) {
			session("project", "P0");
		}
		Project project = Project.find.byId(session("project"));
		Result page=null;
		String resume;
		String picture;
		if(session("language")!=null && session("language").equals("french")) {
			resume= project.getResumeFr();
		}else {
			resume= project.getResumeEn();
		}
		if(session("user")!=null) {
			
			switch(session("user")) {

				case "admin": 	page= ok(views.html.templates.TemplateAdmin.render("Home",views.html.homes.HomeManagement.render(resume,project.getName())));
								break;

				case "owner":	if(Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
									page= ok(views.html.templates.TemplateOwner.render("Home",views.html.homes.HomeManagement.render(resume,project.getName())));
								}else {
									page= ok(views.html.templates.TemplateUser.render("Home",views.html.homes.Home.render(resume,project.getName())));
								}
								break;
				

				case "user": 	page= ok(views.html.templates.TemplateUser.render("Home",views.html.homes.Home.render(resume,project.getName())));
								break;
					

				default : 		page= ok(views.html.templates.Template.render("Home",views.html.homes.Home.render(resume,project.getName())));

			}
			
		} else {
			page= ok(views.html.templates.Template.render("Home",views.html.homes.Home.render(resume,project.getName())));
		}
		
		return page;
		
	}
	
	public Result homes(String id) {
		
		if(Project.find.byId(id)!=null) {
			session("project", id);
		}else {

			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.POJECT_DO_NOT_EXIST));
		}

		if(session("project")==null) {
			session("project", "P0");
		}
		Project project = Project.find.byId(session("project"));
		Result page=null;
		String resume;
		String picture;
		
		if(session("language")!=null && session("language").equals("french")) {
			resume= project.getResumeFr();
		}else {
			resume= project.getResumeEn();
		}
		if(session("user")!=null) {
			
			switch(session("user")) {

				case "admin": 	page= ok(views.html.templates.TemplateAdmin.render("Home",views.html.homes.HomeManagement.render(resume,project.getName())));
								break;

				case "owner":	
								if(Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
									page= ok(views.html.templates.TemplateOwner.render("Home",views.html.homes.HomeManagement.render(resume,project.getName())));
								}else {
									page= ok(views.html.templates.TemplateUser.render("Home",views.html.homes.Home.render(resume,project.getName())));
								}
								break;
				

				case "user": 	page= ok(views.html.templates.TemplateUser.render("Home",views.html.homes.Home.render(resume,project.getName())));
								break;
					

				default : 		page= ok(views.html.templates.Template.render("Home",views.html.homes.Home.render(resume,project.getName())));

			}
			
		} else {
			page= ok(views.html.templates.Template.render("Home",views.html.homes.Home.render(resume,project.getName())));
		}
		
		return page;
		
	}
	
	public Result changeLanguage(String language) {
		if(session("project") == null) {
			session("project","P0");
		}
		session("language",language);
		return redirect(routes.HomeController.home());
	}
	
	public Result editHome() {
		if(session("project") == null) {
			session("project","P0");
		}
		Project project = Project.find.byId(session("project"));
		Result page=null;
		
		if(session("user")!=null) {
			switch(session("user")) {

				case "admin": 	page= ok(views.html.templates.TemplateAdmin.render("Edit Home",views.html.homes.EditHome.render(project)));
								break;
								
				case "owner":	
								if(Project.find_project_owner(Owner.getOwnerByCIP(session("name")))) {
									page= ok(views.html.templates.TemplateOwner.render("Edit Home",views.html.homes.EditHome.render(project)));
								}else {
									flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
									page= redirect(routes.HomeController.home());
								}
								break;
					
				default : 		page= redirect(routes.HomeController.home());
								flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
								break;
			
			}
		} else {
			page= redirect(routes.HomeController.home());
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.REGISTER_FORM_NOT_ADMIN));
		}
		return page;
	} 
	
	public Result postEditHome() {
		
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		Http.MultipartFormData<File> formData;
		Http.MultipartFormData.FilePart<File> filePart;
		Project project = Project.find.byId(session("project"));
		String resumeFr;
		String resumeEn;
		String namePicture;
		String routePicture;
		if (boundForm.hasErrors()) {
			flash(ConstanteMessagesFlash.ERROR, ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			return redirect(routes.HomeController.editHome());

		} else {
			if(boundForm.get("send").equals("send")) {
				
				resumeEn = boundForm.get("resumeEn");
				project.setResumeEn(resumeEn);				
				project.save();
				
				resumeFr = boundForm.get("resumeFr");
				project.setResumeFr(resumeFr);
				project.save();
				
				flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITHOME_OK));
				
			}else {
				flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.EDITHOME_CANCEL));
			}
			return redirect(routes.HomeController.home());
			
		}
	}
}