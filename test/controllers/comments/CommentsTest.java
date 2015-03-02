package controllers.comments;

import models.Comment;
import models.Post;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;
import controllers.BasicFunctionalTest;

public class CommentsTest extends BasicFunctionalTest{

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
		
		assertContentEquals("[{\"id\":"+comment.id+",\"content\":\"Funny comment!\",\"author\":{\"id\":" + comment.author.id + ",\"displayName\":\"Superman\",\"loginName\":\"franky\"},\"creationTimestamp\":1238025600000}]", response);
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
		
		Request authRequest = getAuthRequest("/posts/" + post.id + "/comments", "application/json", "{\"content\":\"test comment content 1\"}", "POST");
		Response response = POST(authRequest, "/posts/" + post.id + "/comments", "application/json", "{\"content\":\"test comment content 1\"}");
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
		
		assertHeaderEquals("Location", authRequest.host + "/comments/" + comment.id, response);
	}
}
