package controllers;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import models.Post;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PostsTest extends FunctionalTest {

	@Before
	public void beforeTest(){
		Fixtures.deleteAllModels();
	}
	
    @Test
    public void testPostCreation() {
        Response response = POST("/posts", 
        	new HashMap(){{
        		put("content", "test post content");
        	}}
        );
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("UTF-8", response);
        String responseBody = response.out.toString();
        Post post = new Gson().fromJson(responseBody, Post.class);
        assertNotNull(post.id);
        assertEquals("test post content", post.content);
        assertNotNull(post.creationDate);
    }

    @Test
    public void testGetAllPosts(){
    	new Post("test content 1").save();
    	new Post("test content 2").save();
    	Response response = GET("/posts");
        assertContentType("application/json", response);
        assertCharset("UTF-8", response);
        String responseBody = response.out.toString();
        
        Type listType = new TypeToken<List<Post>>() {}.getType();
        List<Post> postsList = new Gson().fromJson(responseBody, listType);
        assertEquals(2, postsList.size());
        Post postOne = postsList.get(0);
        assertEquals("test content 1", postOne.content);
        Post postTwo = postsList.get(1);
        assertEquals("test content 2", postTwo.content);
    }
    
}