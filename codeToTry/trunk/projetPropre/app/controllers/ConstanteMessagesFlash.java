package controllers;

import play.mvc.Controller;

public class ConstanteMessagesFlash extends Controller{
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	
	public static final String ERROR_FORM_MESSAGE = "Sorry there was a problem during the execution of this action";
	public static final String ERROR_CONNECTION_MESSAGE = "Sorry but your CIP or your password are incorrect, please try again";
	public static final String CONNECT_MESSAGE = "Connected";
	public static final String DISCONNECT_MESSAGE = "Disconnected";
	public static final String ERROR_FORM_MESSAGE_FR = "Désolé il y a eu un problème lors de l'exécution de cette action";
	public static final String ERROR_CONNECTION_MESSAGE_FR = "Désolé mais votre CIP ou votre mot de passe est incorect, veuillez réessayer";
	public static final String CONNECT_MESSAGE_FR = "Connecté";
	public static final String DISCONNECT_MESSAGE_FR = "Déconnecté";
	
	
	public static final String MAILNOTFOUND = "E-mail not found";
	public static final String MAILSEND = "E-mail sent";
	public static final String OUTOFTIME = "Code available only for 5 minutes";
	public static final String MAILSDIFFERENT = "Both passwords should be the same";
	public static final String MAILNOTFOUND_FR = "E-mail introuvable";
	public static final String MAILSEND_FR = "E-mail envoyé";
	public static final String OUTOFTIME_FR = "Code valable pour 5 minutes seulement";
	public static final String MAILSDIFFERENT_FR = "Les deux mots de passe doivent etre identiques";
	
	public static final String HOST = "smtp.office365.com";
	public static final String FROM = "stage.usherbrooke.cv@outlook.fr";
	public static final String MAILBOXWEBSITENAME = "stage.usherbrooke.cv@outlook.fr";
	public static final String MAILBOXWEBSITEPASSWORD = "stagecv4";
	
	public static final String REGISTER_OK = "Register complete";
	public static final String REGISTER_NOT_OK = "This CIP is already registered";
	public static final String REGISTER_FORM_NOT_ADMIN = "You are not authorized to access to this page";
	public static final String REGISTER_OK_FR = "Enregistrement validé";
	public static final String REGISTER_NOT_OK_FR = "Le CIP entré est déjà utilisé";
	public static final String REGISTER_FORM_NOT_ADMIN_FR = "Vous n'êtes pas autorisé àacceder àcette page";
	
	
	public static final String MODIFICATION_OK = "Profile has been modified";
	public static final String EDITHOME_OK = "Edit has been saved";
	public static final String EDITTEAM_OK = "Team has been modified";
	public static final String EDITHOME_CANCEL ="Operation canceled";
	public static final String MODIFICATION_OK_FR = "Profil modifié";
	public static final String EDITHOME_OK_FR = "La page a bien été modifiée";
	public static final String EDITTEAM_OK_FR = "L'équipe a bien été modifiée";
	public static final String EDITHOME_CANCEL_FR ="Opération annulée";
	
	public static final String ERROR_PROJECT = "This project deoes not exist";
	public static final String ERROR_PROJECT_FR = "Ce projet n'existe pas";
	public static final String ERROR_PROJECT_RIGHT = "You are not the owner of this project";
	public static final String ERROR_PROJECT_RIGHT_FR = "Vous n'êtes pas propriétaire de ce projet";
	public static final String POJECT_DO_NOT_EXIST = "This project does not exist";
	public static final String POJECT_DO_NOT_EXIST_FR = "Ce projet n'éxiste pas";
	
	public static String message(String message) {
		String ret;
		if(session("language")!=null && session("language").equals("french")) {
			switch(message) {
				case ERROR_FORM_MESSAGE:ret=ERROR_FORM_MESSAGE_FR;
				break;
				case ERROR_CONNECTION_MESSAGE:ret=ERROR_FORM_MESSAGE_FR;
				break;
				case CONNECT_MESSAGE:ret=CONNECT_MESSAGE_FR;
				break;
				case DISCONNECT_MESSAGE:ret=DISCONNECT_MESSAGE_FR;
				break;
				
				case MAILNOTFOUND:ret=MAILNOTFOUND_FR;
				break;
				case MAILSEND:ret=MAILSEND_FR;
				break;
				case OUTOFTIME:ret=OUTOFTIME_FR;
				break;
				case MAILSDIFFERENT:ret=MAILSDIFFERENT_FR;
				break;
				
				case REGISTER_OK:ret=REGISTER_OK_FR;
				break;
				case REGISTER_NOT_OK:ret=REGISTER_NOT_OK_FR;
				break;
				case REGISTER_FORM_NOT_ADMIN:ret=REGISTER_FORM_NOT_ADMIN_FR;
				break;
				
				case MODIFICATION_OK:ret=MODIFICATION_OK_FR;
				break;
				case EDITHOME_OK:ret=EDITHOME_OK_FR;
				break;
				case EDITTEAM_OK:ret=EDITTEAM_OK_FR;
				break;
				case EDITHOME_CANCEL:ret=EDITHOME_CANCEL_FR;
				break;
				
				case ERROR_PROJECT_RIGHT:ret=ERROR_PROJECT_RIGHT_FR;
				break;
				case ERROR_PROJECT: ret=ERROR_PROJECT_FR;
				break;
				case POJECT_DO_NOT_EXIST:ret=POJECT_DO_NOT_EXIST_FR;
				break;
				
				default : ret= message;
			}
			
			
		}else {
			ret = message;
		}
		
		
		return ret;
	}
}
