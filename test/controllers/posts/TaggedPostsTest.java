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
import controllers.ContentType;
import controllers.HttpMethod;
import controllers.RequestBuilder;

public class TaggedPostsTest extends BasicFunctionalTest {
	
	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetTaggedPosts() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(2, postsList.size());
		assertEquals("#tagged test content 1", postsList.get(0).content);
		assertEquals("test content 4 #tagged.", postsList.get(1).content);
	}

	@Test
	public void testGetTaggedPostsOrderedDesc() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?sort=created_at");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(2, postsList.size());
		assertEquals("test content 4 #tagged.", postsList.get(0).content);
		assertEquals("#tagged test content 1", postsList.get(1).content);
	}

	@Test
	public void testGetTaggedPostsLimited() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?limit=1");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(1, postsList.size());
		assertEquals("#tagged test content 1", postsList.get(0).content);
	}

	@Test
	public void testGetTaggedPostsPaginated() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?from=1&limit=1");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(1, postsList.size());
		assertEquals("test content 4 #tagged.", postsList.get(0).content);
	}

	@Test
	public void testGetPostTaggedInUkrainian() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/тег");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(1, postsList.size());
		assertEquals("Ще одненький #тег українською", postsList.get(0).content);
	}
	
	@Test
	public void testGetTaggedPostsCount() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged/count");
		assertEquals("{\"count\":2}", response.out.toString());
	}
}