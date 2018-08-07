package TestsModel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Article;
import play.Application;
import play.test.WithApplication;

public class ArticleTest extends WithApplication{
	
	private final String TITLE = "Test Title";

	private Article articleTest;
	private int idtest;
	public static Application app;
	  
	@Before
	public void initArticle() {
			int max;
			articleTest = new Article(TITLE);
			articleTest.save();
			if (Article.find.all().size() != 0) {
				max =Article.find.all().get(Article.find.all().size() - 1).getId();
				for(int i =0 ; i<Article.find.all().size();i++) {
					if(Article.find.all().get(i).getId()>max) {
						max =Article.find.all().get(i).getId();
					}
				}
				this.idtest = max;
			}else {
				this.idtest=0;
			}
		
	}
	
	@Test
	public void instanceArticleTest() {
		assertNotNull("The instance is not created", articleTest);
	}
	
	@Test
	public void numberInstanceTest() {
		assertEquals("Bad Id in this instance",idtest,articleTest.getId());
	}
	
	@Test
	public void titleInstanceTest() {
		assertEquals("Bad Title in this instance",TITLE, articleTest.getTitle());
	}

	@Test
	public void tupleArticleTest() {
		assertNotNull("Article not find in the database", Article.find.byId(idtest));
	}
	
	@Test
	public void tupleArticleNumberTest() {
		assertEquals("Bad Id in the database",idtest,Article.find.byId(idtest).getId());
	}
	
	@Test
	public void tupleArticleTitleTest() {
		assertEquals("Bad Title in the databasee",TITLE, Article.find.byId(idtest).getTitle());
	}
	
	@After
	public void endArticleTest() {
		
		articleTest.delete();
		articleTest=null;
		
	}
}
