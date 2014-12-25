package controllers.comments;

import models.Comment;
import models.Post;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

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
		
		assertContentEquals("[{\"id\":"+comment.id+",\"content\":\"Funny comment!\",\"author\":{\"id\":" + comment.author.id + ",\"displayName\":\"Superman\"},\"creationTimestamp\":1238025600000}]", response);
	}
}
