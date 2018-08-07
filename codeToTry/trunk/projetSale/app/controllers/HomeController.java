package controllers;


import models.Article;
import play.mvc.Controller;
import play.mvc.Result;

public class HomeController extends Controller {
	
	public Result home() {
		return ok(views.html.Home.render("Home", Article.getLasts()));
		
	}
	
}