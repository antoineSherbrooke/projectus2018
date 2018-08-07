package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import io.ebean.*;
import play.data.validation.Constraints;


@Entity
public class Article extends Model{

	public static final Finder<Integer, Article> find = new Finder<Integer, Article>(Article.class);

	@Id
	private int id;

	@Constraints.Required
	private String title;

	public Article(Integer id, String title) {

		this.id = id;
		this.title = title;

	}

	public int getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public static List<Article> getLasts() {
		
		List<Article> listArticleTmp = Article.find.all();
		List<Article> lastArticles = new ArrayList<Article> ();
		
		if(listArticleTmp.size() != 0) {
			
			for(int i = listArticleTmp.size()-1; i>listArticleTmp.size()-11;i--){
				
				lastArticles.add(listArticleTmp.get(i));
				
			}
			
		}
		
		return lastArticles;
		
	}
}
