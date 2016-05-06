package controllers.posts.service.impl;

import controllers.posts.service.PostsService;
import models.Post;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.FunctionalTest;

import java.util.List;

public class PostsServiceImplTest extends FunctionalTest {

    private PostsService postsService = new PostsServiceImpl();

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testGetRecommendedPostsOrderedBySimilarity() {
        Fixtures.loadModels("data/post-rec-order-by-similarity.yml");
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 0);

        assertEquals(3, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Math! N2", postsList.get(1).content);
        assertEquals("Post about Flowers! N3", postsList.get(2).content);
    }

    @Test
    public void testThresholdMatters(){
        Fixtures.loadModels("data/post-rec-threshold.yml");
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 2);

        assertEquals(2, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Flowers! N3", postsList.get(1).content);
    }
}