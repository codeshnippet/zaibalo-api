package controllers.posts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Util;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class PostsTest extends FunctionalTest {

	private static final int NOT_EXISTING_POST_ID = 133454552;

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testPostCreation() {
		Fixtures.loadModels("data/user.yml");

		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "random-auth-token-123"));

		Response response = POST(request, "/posts", "application/json", "{\"content\":\"test post content\"}");
		assertIsOk(response);
		assertContentType("application/json", response);

		assertEquals(1, Post.findAll().size());

		Post post = Post.find("byContent", "test post content").first();
		assertNotNull(post);
		assertNotNull(post.id);
		assertEquals("test post content", post.content);
		assertNotNull(post.creationDate);
		assertNotNull(post.author);
	}

	@Test
	public void testGetAllPosts() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts");
		assertContentType("application/json", response);
		assertCharset("UTF-8", response);

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(2, postsList.size());
		assertEquals("test content 1", postsList.get(0).content);
		assertEquals("test content 2", postsList.get(1).content);
	}

	@Test
	public void testGetPostJsonFormat() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Response response = GET("/posts/" + post.getId());
		assertEquals("{" + "\"id\":" + post.getId() + "," + "\"content\":\"test content 1\"," + "\"author\":{\"id\":" + post.author.id + ",\"displayName\":\"Superman\"}," + "\"creationTimestamp\":1238025600000" + "}",
				response.out.toString());
	}

	@Test
	public void testGetPostNotFound() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts/" + NOT_EXISTING_POST_ID);
		assertIsNotFound(response);
	}

	@Test
	public void testPostCreationIsSecured() {
		Response response = POST("/posts", "application/json", new GsonBuilder().create().toJson(new PostRequest("test post content")));
		assertStatus(401, response);
	}

	@Test
	public void testPostEditingIsSecured() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();
		Response response = PUT("/posts/" + post.id, "application/json", new GsonBuilder().create().toJson(new PostRequest("new post content")));
		assertStatus(401, response);
	}

	@Test
	public void testPostCanBeEditedByOwnerOnly() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "billy-auth-token-123"));
		Response response = PUT(request, "/posts/" + post.id, "application/json", new GsonBuilder().create().toJson(new PostRequest("new post content")));
		assertStatus(403, response);
	}

	@Test
	public void testPostEditing() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "random-auth-token-123"));
		Response response = PUT(request, "/posts/" + post.id, "application/json", new GsonBuilder().create().toJson(new PostRequest("new post content")));
		assertIsOk(response);
		post.refresh();
		assertEquals("new post content", post.content);
	}

	@Test
	public void testPostDeletingIsSecured() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();
		Response response = DELETE("/posts/" + post.id);
		assertStatus(401, response);
	}

	@Test
	public void testPostCanBeDeletedByOwnerOnly() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "billy-auth-token-123"));
		Response response = DELETE(request, "/posts/" + post.id);
		assertStatus(403, response);
	}

	@Test
	public void testPostDeleting() {
		Fixtures.loadModels("data/posts.yml");
		int count = Post.findAll().size();
		assertEquals(2, count);
		Post post = Post.find("byContent", "test content 1").first();

		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "random-auth-token-123"));
		Response response = DELETE(request, "/posts/" + post.id);
		assertIsOk(response);

		count = Post.findAll().size();
		assertEquals(1, count);
	}
}