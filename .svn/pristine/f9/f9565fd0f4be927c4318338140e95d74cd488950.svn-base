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
public class MailController extends Controller {

	private final String ERROR = "error";
	private final String SUCCESS = "success";
	private final String MAILNOTFOUND = "mail not found";
	private final String MAILSEND = "mail send";
	private final String OUTOFTIME = "Code available only for 5 minutes";
	private final String MAILSDIFFERENT = "Both pass should be the same";
	
	private final String HOST = "smtp.office365.com";
	private final String FROM = "stage.USherbrooke.cv@outlook.fr";
	private final String MAILBOXWEBSITENAME = "stage.USherbrooke.cv@outlook.fr";
	private final String MAILBOXWEBSITEPASSWORD = "stagecv4";
	
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

	public Result forget() {
		
		if (session().get("name") != null) {
			return redirect(routes.HomeController.home());
			
		} else {

			return ok(views.html.PasswordForgot.render("Password Forgot"));
		}

	}

	public Result valider() {

		String sendTo;
		String token;	
		String pseudo;
		LostPassword tok;
		DynamicForm boundForm = formFactory.form().bindFromRequest();

		if (boundForm.hasErrors()) {

			return ok(views.html.PasswordForgot.render("Password Forgot"));

		} else {

			sendTo = boundForm.get("mail");
			
			if (Users.mailPresents(sendTo)) {
				
				token = generateCode();
				pseudo = Users.getPseudoByMail(sendTo);
				tok = new LostPassword(token, pseudo);
				tok.save();
				
				this.sendMail(token, sendTo);
				
				flash(SUCCESS, MAILSEND);
				return redirect(routes.LoginController.formulaire());

			} else {
				
				flash(ERROR, MAILNOTFOUND);
				return redirect(routes.MailController.forget());

			}
		}
	}
	
	private void sendMail(String token, String sendTo) {
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(MAILBOXWEBSITENAME, MAILBOXWEBSITEPASSWORD);

			}

		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
			message.setSubject("New Password");
			message.setText("link available for 5 minutes -> localhost:9000/ChangePassword/" + token);
			Transport.send(message);

		} catch (MessagingException e) {

			throw new RuntimeException(e);

		}
	
	}

	public Result passwordChanged(String id) {
		
		if (LostPassword.findById(id).getDateOfLapse().after(Calendar.getInstance())) {
			
			return ok(views.html.ContentPwChange.apply(id));
			
		}else {
			
			flash(ERROR, OUTOFTIME);
			return ok(views.html.PasswordForgot.render("Password Forgot"));
			
		}
	}

	public Result changePassword(String id) {
		
		LostPassword usersID = LostPassword.find.byId(id);
		Users users = Users.find.byId(usersID.getPseudo());
		DynamicForm boundForm = formFactory.form().bindFromRequest();
		String newPassword;

		if (boundForm.hasErrors()) {

			return ok(views.html.ContentPwChange.apply(id));

		} else {
			
			if (!boundForm.get("pass").equals(boundForm.get("passVerif"))) {
				
				flash(ERROR,MAILSDIFFERENT);
				return ok(views.html.ContentPwChange.apply(id));
				
			} else {
				
				newPassword = boundForm.get("pass");
				users.setPassword(newPassword);
				users.save();
				return redirect(routes.LoginController.formulaire());
				
			}
			
		}
		
	}

}
