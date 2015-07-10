package controllers.commentratings;

import models.Comment;
import models.CommentRating;
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
import controllers.comments.CommentRequest;
import controllers.rating.RatingRequest;

public class CommentRatingsTest
    extends FunctionalTest {

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testCommentRatingCreation() {
        Fixtures.loadModels("data/comment-ratings.yml");
        Comment comment = Comment.find("byContent", "Funny comment!").first();
        User user = User.findByLoginName("theresa");

        Response response = new RequestBuilder()
            .withPath("/comments/" + comment.id + "/comment-ratings")
            .withHttpMethod(HttpMethod.POST)
            .withContentType(ContentType.APPLICATION_JSON)
            .withBody("{\"isPositive\":false}")
            .withUsername("theresa")
            .withToken("secret_token_789")
            .send();

        assertStatus(201, response);
        assertContentType("application/json", response);

        assertEquals(4, CommentRating.findAll().size());

        CommentRating commentRating = CommentRating.find("byUser", user).first();
        assertNotNull(commentRating);
        assertNotNull(comment.creationDate);
        assertNotNull(commentRating.comment);
        assertEquals(comment, commentRating.comment);
        assertNotNull(commentRating.user);
        assertEquals(user, commentRating.user);
        assertEquals(false, commentRating.isPositive());

        comment.refresh();
        assertEquals(Integer.valueOf(4), comment.getRatingCount());
        assertEquals(Integer.valueOf(0), comment.getRatingSum());

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

        assertStatus(204, response);

        comment.refresh();
        assertEquals(Integer.valueOf(2), comment.getRatingCount());
        assertEquals(Integer.valueOf(0), comment.getRatingSum());
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
        assertEquals(Integer.valueOf(3), comment.getRatingCount());
        assertEquals(Integer.valueOf(1), comment.getRatingSum());
    }
}
