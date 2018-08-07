package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PersonController extends Controller{
	private final Form<PersonData> formulaire;

	
    @Inject
    public PersonController(FormFactory formFactory) {
        this.formulaire = formFactory.form(PersonData.class);

    }
	public Result person() {
		return ok(views.html.Person.render("Test de création de page"));
	}
	public Result formulaire() {
		if(session("name") != null) {
			return ok(views.html.formulaire.render(formulaire));
			
		}else {
			return ok(views.html.formulaire.render(formulaire));
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public Result fait() {
			final Form<PersonData> boundForm = formulaire.bindFromRequest();

	        if (boundForm.hasErrors()) {
	        	
	            play.Logger.ALogger logger = play.Logger.of(getClass());
	            logger.error("errors = {}", boundForm.errors());
	            return redirect(routes.PersonController.formulaire());
	            
	        } else {
	        	
	        	PersonData data = boundForm.get();
	        	
	
	        	if(session("name")==null) {
		        	session("name",data.getName());

		            session("mdp",data.getMdp());
		            
		            if(data.getMdp().contentEquals("oui")) {
		            	return redirect(routes.PersonController.formulaire());
		            }else {
		            	session().remove("name");
		            	return redirect(routes.PersonController.formulaire());
		            }
		            
	        	}else {
	        		return redirect(routes.PersonController.formulaire());
	        	}

	        }

	}
	public Result deconnection() {
		session().remove("name");
		session().remove("mdp");
		return redirect(routes.PersonController.formulaire());
	}
}
