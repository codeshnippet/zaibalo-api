package controllers.posts;

import controllers.HttpMethod;
import controllers.RequestBuilder;
import org.junit.Test;
import play.mvc.Http;
import play.test.Fixtures;

import java.util.List;

public class RecommendedPostsTest extends AbstractPostsTest {

    @Test
    public void testGetRecommendedPosts() {
        Fixtures.loadModels("data/post-recommendation.yml");

        Http.Response response = new RequestBuilder()
                .withPath("/posts/recommended")
                .withHttpMethod(HttpMethod.GET)
                .send();

        assertStatus(200, response);
        assertContentType("application/json", response);
        List<PostResource> postsList = getPostsListFrom(response);

        assertNotNull(postsList);
        assertEquals(1, postsList.size());
        PostResource postOne = postsList.get(0);
        assertEquals("Post about Math!", postOne.content);
    }

}
