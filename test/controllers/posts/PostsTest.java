package controllers.posts;

import java.util.List;

import models.Post;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import utils.HalGsonBuilder;
import ch.halarious.core.HalResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controllers.ContentType;
import controllers.HttpMethod;
import controllers.RequestBuilder;

public class PostsTest extends AbstractPostsTest {

	private static final String BILLY_SECRET_TOKEN = "secret_token_321";
	private static final String BILLY_USERNAME = "billy";
	private static final int NOT_EXISTING_POST_ID = 133454552;

	@Test
	public void testPostCreation() {
		Fixtures.loadModels("data/user.yml");

		Response response = new RequestBuilder()
			.withPath("/posts")
			.withHttpMethod(HttpMethod.POST)
			.withContentType(ContentType.APPLICATION_JSON)
			.withBody("{\"content\":\"test post content\"}")
			.send();

		assertStatus(201, response);
		assertContentType("application/json", response);
		assertEquals(1, Post.findAll().size());

		Post post = Post.find("byContent", "test post content").first();
		assertNotNull(post);
		assertNotNull(post.id);
		assertEquals("test post content", post.content);
		assertNotNull(post.creationDate);
		assertNotNull(post.author);

		assertHeaderEquals("Location", newRequest().host + "/posts/" + post.id, response);
	}

	@Test
	public void testGetAllPosts() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts");

		assertContentType("application/json", response);
		assertCharset("UTF-8", response);

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(2, postsList.size());
		
		PostResource postOne = postsList.get(0);
		assertEquals("test content 2", postOne.content);
		assertEquals(0, postOne.attachments.size());

		PostResource postTwo = postsList.get(1);
		assertEquals("test content 1", postTwo.content);
		assertEquals(1, postTwo.attachments.size());
	}

	@Test
	public void testGetPostsOrderedDesc() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts?sort=created_at");

		List<PostResource> postsList = getPostsListFrom(response);
		assertEquals(2, postsList.size());
		assertEquals("test content 2", postsList.get(0).content);
		assertEquals("test content 1", postsList.get(1).content);
	}

	@Test
	public void testGetPostsLimited() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts?limit=1");

		List<PostResource> postsList = getPostsListFrom(response);
		assertEquals(1, postsList.size());
		assertEquals("test content 2", postsList.get(0).content);
	}

	@Test
	public void testGetPostsPaginated() {
		Fixtures.loadModels("data/posts.yml");

		Response response = GET("/posts?from=1&limit=1");

		List<PostResource> postsList = getPostsListFrom(response);
		assertEquals(1, postsList.size());
		assertEquals("test content 1", postsList.get(0).content);
	}

	@Test
	public void testGetSinglePost() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		Response response = GET("/posts/" + post.getId());
		PostsListResource json = getPostsListResource(response);
		assertEquals(post.getId().longValue(), json.posts.get(0).id);
		assertEquals("test content 1", json.posts.get(0).content);
		assertEquals(post.author.id.longValue(), json.posts.get(0).author.id);
		assertEquals(post.author.getDisplayName(), json.posts.get(0).author.displayName);
		assertEquals(1238025600000l, json.posts.get(0).creationTimestamp);
		assertNull(json.posts.get(0).comments);
		assertEquals(2, json.posts.get(0).ratingSum);
		assertEquals(2, json.posts.get(0).ratingCount);
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
		String body = new GsonBuilder().create().toJson(new PostRequest("new post content"));
		Response response = PUT("/posts/" + post.id, "application/json", body);
		assertStatus(401, response);
	}

	@Test
	public void testPostCanBeEditedByOwnerOnly() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		String bodyJson = new GsonBuilder().create().toJson(new PostRequest("new post content"));
		Response response = new RequestBuilder()
			.withPath("/posts/" + post.id)
			.withHttpMethod(HttpMethod.PUT)
			.withContentType(ContentType.APPLICATION_JSON)
			.withBody(bodyJson)
			.withUsername(BILLY_USERNAME)
			.withToken(BILLY_SECRET_TOKEN)
			.send();
		
		assertStatus(403, response);
	}

	@Test
	public void testPostEditing() {
		Fixtures.loadModels("data/posts.yml");

		Post post = Post.find("byContent", "test content 1").first();

		String bodyJson = new GsonBuilder().create().toJson(new PostRequest("new post content"));
		
		Response response = new RequestBuilder()
		.withPath("/posts/" + post.id)
		.withHttpMethod(HttpMethod.PUT)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(bodyJson)
		.send();
		
		assertIsOk(response);
		post.refresh();
		assertEquals("new post content", post.content);

		PostResource postResponse = new GsonBuilder().create().fromJson(response.out.toString(), PostResource.class);
		assertEquals(post.id, Long.valueOf(postResponse.id));
		assertEquals("new post content", postResponse.content);
		assertNotNull(postResponse.author);
		assertEquals("Superman", postResponse.author.displayName);
		assertEquals(post.author.id, Long.valueOf(postResponse.author.id));
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

		Response response = new RequestBuilder()
			.withPath("/posts/" + post.id)
			.withHttpMethod(HttpMethod.DELETE)
			.withUsername(BILLY_USERNAME)
			.withToken(BILLY_SECRET_TOKEN)
			.send();
		
		assertStatus(403, response);
	}

	@Test
	public void testPostDeleting() {
		Fixtures.loadModels("data/posts.yml");

		int count = Post.findAll().size();
		assertEquals(2, count);
		Post post = Post.find("byContent", "test content 1").first();

		Response response = new RequestBuilder()
			.withPath("/posts/" + post.id)
			.withHttpMethod(HttpMethod.DELETE)
			.send();
		
		assertIsOk(response);

		count = Post.findAll().size();
		assertEquals(1, count);
	}
	
	@Test
	public void testEmptyPostCreationFails() {
		Fixtures.loadModels("data/user.yml");

		Response response = new RequestBuilder()
			.withPath("/posts")
			.withHttpMethod(HttpMethod.POST)
			.withContentType(ContentType.APPLICATION_JSON)
			.withBody("{\"content\":\"\"}")
			.send();

		assertStatus(400, response);
	}

    @Test
    public void testNextLinkPresent(){
        Fixtures.loadModels("data/posts.yml");

        Response response = GET("/posts?from=0&limit=1");

        PostsListResource resource = getPostsListResource(response);
        assertNotNull(resource.next);
        assertEquals("/posts?from=1&limit=1", resource.next);
    }

    @Test
    public void testNextLinkNotPresentWhenLastPageShown(){
        Fixtures.loadModels("data/posts.yml");

        Response response = GET("/posts?from=1&limit=1");

        PostsListResource resource = getPostsListResource(response);
        assertNull(resource.next);
    }

}