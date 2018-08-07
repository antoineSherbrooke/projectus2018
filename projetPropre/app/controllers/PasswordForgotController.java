package controllers;

import java.util.Calendar;
import java.util.Properties;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import models.LostPassword;
import models.Users;

@Singleton
public class PasswordForgotController extends Controller {

	
	@Inject
	private FormFactory formFactory;

	public String generateCode() {

		String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
		String hash = "";

		for (int nbCaractere = 0; nbCaractere < 20; nbCaractere++) {

			int i = (int) Math.floor(Math.random() * (chars.length() - 1));
			hash += chars.charAt(i);
		}

		return hash;
	}

	public Result sendMailResetForm() {
		if(session("project") == null) {
			session("project","P0");
		}
		if (session("name") != null) {
			return redirect(routes.HomeController.home());
			
		} else {

			return ok(views.html.templates.Template.render("Password Forgot",views.html.PasswordForgot.render()));
		}

	}

	public Result sendMailResetPassword() {

		String sendTo;
		String token;	
		String cip;
		LostPassword tok;
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		if(session("project") == null) {
			session("project","P0");
		}
		if (boundForm.hasErrors()) {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			return ok(views.html.templates.Template.render("Password Forgot",views.html.PasswordForgot.render()));

		} else {
			sendTo = boundForm.get("mail");
			
			if (Users.mailPresents(sendTo)) {
				token = generateCode();
				cip = Users.getCipByMail(sendTo);
				tok = new LostPassword(token, cip);
				tok.save();
				this.sendMail(token, sendTo);
				flash(ConstanteMessagesFlash.SUCCESS, ConstanteMessagesFlash.message(ConstanteMessagesFlash.MAILSEND));
				return redirect(routes.LoginController.loginForm());

			} else {
				flash(ConstanteMessagesFlash.ERROR, ConstanteMessagesFlash.message(ConstanteMessagesFlash.MAILNOTFOUND));
				return redirect(routes.PasswordForgotController.sendMailResetForm());

			}
		}
	}
	
	private void sendMail(String token, String sendTo) {
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", ConstanteMessagesFlash.HOST);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(ConstanteMessagesFlash.MAILBOXWEBSITENAME, ConstanteMessagesFlash.MAILBOXWEBSITEPASSWORD);

			}

		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ConstanteMessagesFlash.FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
			message.setSubject("New Password");
			message.setText("link available for 5 minutes -> localhost:9000/ChangePassword/" + token);
			Transport.send(message);
		} catch (MessagingException e) {

			throw new RuntimeException(e);

		}
	
	}

	public Result passwordChanged(String id) {
		if(session("project") == null) {
			session("project","P0");
		}
		if (LostPassword.findById(id).getDateOfLapse().after(Calendar.getInstance())) {
			
			return ok(views.html.templates.Template.render("Change PW",views.html.ChangePassword.apply(id)));
			
		}else {
			
			flash(ConstanteMessagesFlash.ERROR, ConstanteMessagesFlash.message(ConstanteMessagesFlash.OUTOFTIME));
			return ok(views.html.templates.Template.render("Password Forgot",views.html.PasswordForgot.render()));
			
		}
	}

	public Result changePassword(String id) {
		LostPassword usersID = LostPassword.find.byId(id);
		Users  user =Users.find.byId(usersID.getCip());
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		String newPassword;
		if(session("project") == null) {
			session("project","P0");
		}
		if (boundForm.hasErrors()) {
			flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.ERROR_FORM_MESSAGE));
			return ok(views.html.templates.Template.render("Change PW",views.html.ChangePassword.apply(id)));

		} else {
			
			if (!boundForm.get("pass").equals(boundForm.get("passVerif"))) {
				flash(ConstanteMessagesFlash.ERROR,ConstanteMessagesFlash.message(ConstanteMessagesFlash.MAILSDIFFERENT));
				return ok(views.html.templates.Template.render("Change PW",views.html.ChangePassword.apply(id)));
				
			} else {
				newPassword = boundForm.get("pass");
				user.setPassword(newPassword);
				user.save();
				return redirect(routes.LoginController.loginForm());
				
			}
			
		}
		
	}

}
