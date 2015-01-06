package controllers.posts;

import java.util.List;

import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import controllers.BasicFunctionalTest;

public class PostsTest extends BasicFunctionalTest {

	private static final int NOT_EXISTING_POST_ID = 133454552;

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testPostCreation() {
		Fixtures.loadModels("data/user.yml");
		
		String bodyJson = "{\"content\":\"test post content\"}";
		String url = "/posts";
		Request request = getAuthRequest(url, "application/json", bodyJson, "POST");

		Response response = POST(request, url, "application/json", bodyJson);
		assertStatus(201, response);
		assertContentType("application/json", response);
		assertEquals(1, Post.findAll().size());

		Post post = Post.find("byContent", "test post content").first();
		assertNotNull(post);
		assertNotNull(post.id);
		assertEquals("test post content", post.content);
		assertNotNull(post.creationDate);
		assertNotNull(post.author);
		
		assertHeaderEquals("Location", request.host + "/posts/" + post.id, response);
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
	public void testGetPostsOrderedDesc() {
		Fixtures.loadModels("data/posts.yml");
		
		Response response = GET("/posts?sort=created_at");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(2, postsList.size());
		assertEquals("test content 2", postsList.get(0).content);
		assertEquals("test content 1", postsList.get(1).content);
	}
	
	@Test
	public void testGetPostsLimited() {
		Fixtures.loadModels("data/posts.yml");
		
		Response response = GET("/posts?limit=1");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(1, postsList.size());
		assertEquals("test content 1", postsList.get(0).content);
	}
	
	@Test
	public void testGetPostsPaginated() {
		Fixtures.loadModels("data/posts.yml");
		
		Response response = GET("/posts?page=2&limit=1");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(1, postsList.size());
		assertEquals("test content 2", postsList.get(0).content);
	}

	@Test
	public void testGetPostJsonFormat() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Response response = GET("/posts/" + post.getId());
		assertEquals("{" + "\"id\":" + post.getId() + "," + "\"content\":\"test content 1\",\"author\":{\"id\":" + post.author.id + ",\"displayName\":\"Superman\"},\"creationTimestamp\":1238025600000" + "}",
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

		String bodyJson = new GsonBuilder().create().toJson(new PostRequest("new post content"));
		String url = "/posts/" + post.id;
		Request request = getAuthRequest(url, "application/json", bodyJson, "PUT", "billy", "secret");
		
		Response response = PUT(request, url, "application/json", bodyJson);
		assertStatus(403, response);
	}

	@Test
	public void testPostEditing() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		String bodyJson = new GsonBuilder().create().toJson(new PostRequest("new post content"));
		String url = "/posts/" + post.id;
		Request request = getAuthRequest(url, "application/json", bodyJson, "PUT");
		
		Response response = PUT(request, url, "application/json", bodyJson);
		assertIsOk(response);
		post.refresh();
		assertEquals("new post content", post.content);
		
		PostResponse postResponse = new GsonBuilder().create().fromJson(response.out.toString(), PostResponse.class);
		assertEquals(Long.valueOf(post.id), Long.valueOf(postResponse.id));
		assertEquals("new post content", postResponse.content);
		assertNotNull(postResponse.author);
		assertEquals("Superman", postResponse.author.displayName);
		assertEquals(Long.valueOf(post.author.id), Long.valueOf(postResponse.author.id));
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
		
		String url = "/posts/" + post.id;
		Request request = getAuthRequestForUser(url, "DELETE", "billy", "secret");

		Response response = DELETE(request, url);
		assertStatus(403, response);
	}

	@Test
	public void testPostDeleting() {
		Fixtures.loadModels("data/posts.yml");
		
		int count = Post.findAll().size();
		assertEquals(2, count);
		Post post = Post.find("byContent", "test content 1").first();

		String url = "/posts/" + post.id;
		Request request = getAuthRequestForDefaultUser(url, "DELETE");
		
		Response response = DELETE(request, url);
		assertIsOk(response);

		count = Post.findAll().size();
		assertEquals(1, count);
	}
}