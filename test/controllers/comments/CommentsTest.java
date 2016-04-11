package controllers.comments;

import java.util.Random;

import models.Comment;
import models.Post;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import controllers.ContentType;
import controllers.HttpMethod;
import controllers.RequestBuilder;

public class CommentsTest extends FunctionalTest{

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}
	
	@Test
	public void testGetPostComments(){
		Fixtures.loadModels("data/comments.yml");
		Post post = Post.find("byContent", "test content 1").first();
		Comment comment = post.comments.get(0);
		
		Response response = GET("/posts/" + post.id + "/comments");
		assertIsOk(response);
		assertContentType("application/json", response);
		
		assertContentEquals("[{\"id\":"+comment.id+",\"content\":\"Funny comment!\",\"author\":{\"id\":" + comment.author.id + ",\"displayName\":\"Superman\",\"photo\":\"https://s3.eu-central-1.amazonaws.com/z-avatars/default.jpg\",\"loginName\":\"franky\",\"photoProvider\":\"ZAIBALO\"},\"creationTimestamp\":1238025600000,\"ratingSum\":0,\"ratingCount\":0,\"selfRef\":\"/comments/"+comment.id+"\"}]", response);
	}
	
	@Test
	public void testCratePostCommentIsSecured(){
		Fixtures.loadModels("data/posts.yml");
		Post post = Post.find("byContent", "test content 1").first();
		
		Response response = POST("/posts/" + post.id + "/comments", "application/json", "{\"content\":\"test comment content 1\"}");
		assertStatus(401, response);
	}
	
	@Test
	public void testCratePostCommentSuccess(){
		Fixtures.loadModels("data/posts.yml");
		Post post = Post.find("byContent", "test content 1").first();
		
		Response response = new RequestBuilder()
		.withPath("/posts/" + post.id + "/comments")
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody("{\"content\":\"test comment content 1\"}")
		.send();

		assertStatus(201, response);
		
		post.refresh();
		assertEquals(1, post.comments.size());
		Comment comment = post.comments.get(0);
		assertEquals("test comment content 1", comment.content);
		assertNotNull(comment.author);
		assertEquals("franky", comment.author.loginName);
		assertNotNull(comment.post);
		assertEquals("test content 1", comment.post.content);
		assertNotNull(comment.creationDate);
		
		assertHeaderEquals("Location", newRequest().host + "/comments/" + comment.id, response);
	}
	
	@Test
	public void testCrateEmptyCommentFails(){
		Fixtures.loadModels("data/posts.yml");
		Post post = Post.find("byContent", "test content 1").first();
		
		Response response = new RequestBuilder()
		.withPath("/posts/" + post.id + "/comments")
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody("{\"content\":\"\"}")
		.send();

		assertStatus(400, response);
		
	}
	
	@Test
	public void testCommentDeletionSuccess(){
		Fixtures.loadModels("data/comments.yml");
		assertEquals(1, Comment.count());
		Comment comment = Comment.all().first();
		
		Response response = new RequestBuilder()
		.withPath("/comments/" + comment.id)
		.withHttpMethod(HttpMethod.DELETE)
		.send();
		
		assertStatus(200, response);
		assertEquals(0, Comment.count());
	}
	
	@Test
	public void testCommentDeletionIsSecure(){
		Fixtures.loadModels("data/comments.yml");
		assertEquals(1, Comment.count());
		Comment comment = Comment.all().first();
		
		Response response = DELETE("/comments/" + comment.id);
		
		assertStatus(401, response);
		assertEquals(1, Comment.count());
	}
	
	@Test
	public void testCommentCanBeDeletedByOwnerOnly(){
		Fixtures.loadModels("data/comments.yml");
		Comment comment = Comment.all().first();
		
		Response response = new RequestBuilder()
		.withPath("/comments/" + comment.id)
		.withHttpMethod(HttpMethod.DELETE)
		.withUsername("billy")
		.withToken("secret_token_321")
		.send();
		
		assertStatus(403, response);
	}
	
	@Test
	public void testDeletingCommentWithNotExistingId(){
		Fixtures.loadModels("data/comments.yml");

		Response response = new RequestBuilder()
		.withPath("/comments/123")
		.withHttpMethod(HttpMethod.DELETE)
		.send();
		
		assertStatus(404, response);
	}
}
