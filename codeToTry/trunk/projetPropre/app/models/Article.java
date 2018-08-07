package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import io.ebean.*;
import io.ebean.annotation.NotNull;


@Entity
public class Article extends Model{

	public static final Finder<Integer, Article> find = new Finder<Integer, Article>(Article.class);

	@Id
	private int id;

	@NotNull
	private String title;

	public Article(String title) {
		int max;
		if (Article.find.all().size() != 0) {
			max =Article.find.all().get(Article.find.all().size() - 1).getId();
			for(int i =0 ; i<Article.find.all().size();i++) {
				if(Article.find.all().get(i).getId()>max) {
					max =Article.find.all().get(i).getId();
				}
			}
			this.id = max+ 1;
		}else {
			this.id=0;
		}
		this.title = title;

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
	
	public int getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
}
