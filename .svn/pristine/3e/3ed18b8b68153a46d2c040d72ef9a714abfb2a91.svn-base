package TestsModel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Article;
import models.LostPassword;
import play.Application;
import play.test.WithApplication;

public class ArticleTest extends WithApplication{
	private final int ID =0;
	private final String TITLE = "Test Title";

	private Article articleTest;
	
	public static Application app;
	  
	@Before
	public void initArticle() {
		articleTest = new Article(ID,TITLE);
		articleTest.save();
	}
	
	@Test
	public void instanceArticleTest() {
		assertNotNull("The instance is not created", articleTest);
	}
	
	@Test
	public void numberInstanceTest() {
		assertEquals("Bad Id in this instance",ID,articleTest.getId());
	}
	
	@Test
	public void titleInstanceTest() {
		assertEquals("Bad Title in this instance",TITLE, articleTest.getTitle());
	}

	@Test
	public void tupleArticleTest() {
		assertNotNull("Article not find in the database", Article.find.byId(ID));
	}
	
	@Test
	public void tupleArticleNumberTest() {
		assertEquals("Bad Id in the database",ID,Article.find.byId(ID).getId());
	}
	
	@Test
	public void tupleArticleTitleTest() {
		assertEquals("Bad Title in the databasee",TITLE, Article.find.byId(ID).getTitle());
	}
	@After
	public void endArticleTest() {
		articleTest=null;
		Article.find.byId(ID).delete();
	}
}
