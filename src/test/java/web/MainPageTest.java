package web;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fluentlenium.core.annotation.Page;
import org.junit.Test;

import web.pages.IndexPage;

public class MainPageTest extends BaseTest {

    @Page
    public IndexPage indexPage;

    @Test
    public void testThatTagCloudIsDisplayed() {
        goTo(indexPage);
        assertThat(indexPage.getTagsFromTagCloud(),
                hasItems("dropwizard", "elasticsearch", "twitter", "bootstrap"));
    }

    @Test
    public void testThatClickOnTitleShowsOnlyOneEntry() {
        goTo(indexPage);
        indexPage.clickOnFirstArticleTitle();
        assertExistingArticles(1);
    }

    @Test
    public void testThatSearchIsWorking() {
        goTo(indexPage);
        indexPage.search("Technologies");
        assertExistingArticles(1);
        assertArticleTitle("Using Dropwizard with Elasticsearch as backend");
        indexPage.search("second");
        assertExistingArticles(1);
        assertArticleTitle("This is a blog post title");
    }

    private void assertArticleTitle(String title) {
        assertThat(findFirst("h2").getText(), is(title));
    }

    private void assertExistingArticles(int count) {
        await().atMost(1, TimeUnit.SECONDS).until("h2").hasSize(count);
    }
}
