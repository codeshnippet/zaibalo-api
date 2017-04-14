package controllers.commentratings;

import models.Comment;
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
import controllers.rating.RatingRequest;

public class CommentRatingsTest
    extends FunctionalTest {

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testCommentRatingCreationIsSecured() {
        Fixtures.loadModels("data/comment-ratings.yml");
        Comment comment = Comment.find("byContent", "Funny comment!").first();

        Response response = POST("/comments/" + comment.id + "/comment-ratings", "application/json", new GsonBuilder()
            .create()
            .toJson(new RatingRequest(true)));

        assertStatus(401, response);
    }

    @Test
    public void testCommentRatingCreationWithOppositeValue() {
        Fixtures.loadModels("data/comment-ratings.yml");
        Comment comment = Comment.find("byContent", "Funny comment!").first();

        Response response = new RequestBuilder()
        .withPath("/comments/" + comment.id + "/comment-ratings")
        .withHttpMethod(HttpMethod.POST)
        .withContentType(ContentType.APPLICATION_JSON)
        .withBody("{\"isPositive\": false}")
        .send();

        assertStatus(200, response);
    }

    @Test
    public void testCommentRatingCreationWithSameValue() {
        Fixtures.loadModels("data/comment-ratings.yml");
        Comment comment = Comment.find("byContent", "Funny comment!").first();

        Response response = new RequestBuilder()
        .withPath("/comments/" + comment.id + "/comment-ratings")
        .withHttpMethod(HttpMethod.POST)
        .withContentType(ContentType.APPLICATION_JSON)
        .withBody("{\"isPositive\": true}")
        .send();

        assertStatus(400, response);
        assertContentEquals("POST_RATE_EXISTS", response);

        comment.refresh();
    }
}
