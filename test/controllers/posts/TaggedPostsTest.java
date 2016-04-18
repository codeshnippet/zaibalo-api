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

public class TaggedPostsTest extends FunctionalTest {
	
	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetTaggedPosts() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged");

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(2, postsList.size());
		assertEquals("test content 4 #tagged.", postsList.get(0).content);
		assertEquals("#tagged test content 1", postsList.get(1).content);
	}

	@Test
	public void testGetTaggedPostsOrderedDesc() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?sort=created_at");

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(2, postsList.size());
		assertEquals("test content 4 #tagged.", postsList.get(0).content);
		assertEquals("#tagged test content 1", postsList.get(1).content);
	}

	@Test
	public void testGetTaggedPostsLimited() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?limit=1");

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(1, postsList.size());
		assertEquals("test content 4 #tagged.", postsList.get(0).content);
	}

	@Test
	public void testGetTaggedPostsPaginated() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/tagged?from=1&limit=1");

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(1, postsList.size());
		assertEquals("#tagged test content 1", postsList.get(0).content);
	}

	@Test
	public void testGetPostTaggedInUkrainian() {
		Fixtures.loadModels("data/tagged-posts.yml");

		Response response = GET("/posts/hashtag/тег");

		List<PostResource> postsList = getPostsListFrom(response);
		
		assertEquals(1, postsList.size());
		assertEquals("Ще одненький #тег українською", postsList.get(0).content);
	}

	private List<PostResource> getPostsListFrom(Response response) {
		String responseBody = response.out.toString();
		Gson gson = HalGsonBuilder.getDeserializerGson(PostsListResource.class);
		PostsListResource postsListResource = (PostsListResource) gson.fromJson(responseBody, HalResource.class);
		return postsListResource.posts;
	}
}