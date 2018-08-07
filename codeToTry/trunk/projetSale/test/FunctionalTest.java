import org.junit.Test;
import play.test.WithApplication;
import play.twirl.api.Content;

import models.Article;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTest extends WithApplication
{
	@Test
    public void renderHome()
    {
        Content html = views.html.ContentHome.render(Article.getLasts());
        assertThat("text/html").isEqualTo(html.contentType());
        assertThat(html.body()).contains("Home page");
    }
	
	@Test
    public void renderSearch()
    {
        Content html = views.html.ContentSearch.render();
        assertThat("text/html").isEqualTo(html.contentType());
        assertThat(html.body()).contains("Search a CV here");
    }

    @Test
    public void renderEdit()
    {
        Content html = views.html.ContentEdit.render();
        assertThat("text/html").isEqualTo(html.contentType());
        assertThat(html.body()).contains("Edit your CV here");
    }
    
    @Test
    public void renderContact()
    {
        Content html = views.html.ContentContact.render();
        assertThat("text/html").isEqualTo(html.contentType());
        assertThat(html.body()).contains("Contact us");
        assertThat(html.body()).contains("Mail : stage.usherbrooke.cv&#64;outlook.fr");
        assertThat(html.body()).contains("Address : 2500, boulevard de l'Universit&eacute; Sherbrooke (Qu&eacute;bec)  J1K 2R1");
        assertThat(html.body()).contains("Phone : 819 821-8000");
    }
}
