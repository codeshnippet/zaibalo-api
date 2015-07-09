package controllers.postratings;

import models.Post;
import models.PostRating;
import models.User;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import controllers.ContentType;
import controllers.HttpMethod;
import controllers.RequestBuilder;
import controllers.posts.PostRequest;

public class PostRatingsTest
    extends FunctionalTest {

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testPostRatingCreation() {
        Fixtures.loadModels("data/post-ratings.yml");
        Post post = Post.find("byContent", "test content 1").first();
        User user = User.findByLoginName("theresa");

        Response response = new RequestBuilder()
            .withPath("/posts/" + post.id + "/post-ratings")
            .withHttpMethod(HttpMethod.POST)
            .withContentType(ContentType.APPLICATION_JSON)
            .withBody("{\"isPositive\":false}")
            .withUsername("theresa")
            .withToken("secret_token_789")
            .send();

        assertStatus(201, response);
        assertContentType("application/json", response);

        assertEquals(4, PostRating.findAll().size());

        PostRating postRating = PostRating.find("byUser", user).first();
        assertNotNull(postRating);
        assertNotNull(post.creationDate);
        assertNotNull(postRating.post);
        assertEquals(post, postRating.post);
        assertNotNull(postRating.user);
        assertEquals(user, postRating.user);
        assertEquals(false, postRating.isPositive());

        post.refresh();
        assertEquals(4, post.getRatingCount());
        assertEquals(0, post.getRatingSum());

        assertHeaderEquals("Location", newRequest().host + "/post-ratings/" + postRating.id, response);

    }

    @Test
    public void testPostRatingCreationIsSecured() {
        Fixtures.loadModels("data/post-ratings.yml");
        Post post = Post.find("byContent", "test content 1").first();

        Response response = POST("/posts/" + post.id + "/post-ratings", "application/json", new GsonBuilder()
            .create()
            .toJson(new PostRatingRequest(true)));

        assertStatus(401, response);
    }

    @Test
    public void testPostRatingCreationWithOppositeValue() {
        Fixtures.loadModels("data/post-ratings.yml");
        Post post = Post.find("byContent", "test content 1").first();

        Response response = new RequestBuilder()
        .withPath("/posts/" + post.id + "/post-ratings")
        .withHttpMethod(HttpMethod.POST)
        .withContentType(ContentType.APPLICATION_JSON)
        .withBody("{\"isPositive\": false}")
        .send();

        assertStatus(204, response);

        post.refresh();
        assertEquals(2, post.getRatingCount());
        assertEquals(0, post.getRatingSum());
    }

    @Test
    public void testPostRatingCreationWithSameValue() {
        Fixtures.loadModels("data/post-ratings.yml");
        Post post = Post.find("byContent", "test content 1").first();

        Response response = new RequestBuilder()
        .withPath("/posts/" + post.id + "/post-ratings")
        .withHttpMethod(HttpMethod.POST)
        .withContentType(ContentType.APPLICATION_JSON)
        .withBody("{\"isPositive\": true}")
        .send();

        assertStatus(400, response);
        assertContentEquals("POST_RATE_EXISTS", response);

        post.refresh();
        assertEquals(3, post.getRatingCount());
        assertEquals(1, post.getRatingSum());
    }
}
