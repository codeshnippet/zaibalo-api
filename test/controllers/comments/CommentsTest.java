package controllers.comments;

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
		
		assertContentEquals("[{\"id\":"+comment.id+",\"content\":\"Funny comment!\",\"author\":{\"id\":" + comment.author.id + ",\"displayName\":\"Superman\",\"photo\":\"http://avatars.io/18xnkPlshw\",\"loginName\":\"franky\",\"photoProvider\":\"AVATARS_IO\"},\"creationTimestamp\":1238025600000}]", response);
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
}
