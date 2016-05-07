package controllers.posts.service.impl;

import controllers.posts.service.PostsService;
import models.Post;
import models.PostRating;
import models.Similarity;
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
        Fixtures.loadModels("controllers/posts/service/impl/post-rec-threshold.yml");
    }

    @Test
    public void testGetRecommendedPostsOrderedBySimilarity() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 0);

        assertEquals(3, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Math! N2", postsList.get(1).content);
        assertEquals("Post about Flowers! N3", postsList.get(2).content);
    }

    @Test
    public void testThresholdMatters() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 2);

        assertEquals(2, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Flowers! N3", postsList.get(1).content);
    }

    @Test
    public void testPaginatingRecommendedPostsFromOne() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 1, 0);

        assertEquals(1, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
    }

    @Test
    public void testPaginatingRecommendedPostsFromTwo() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 1, 1, 0);

        assertEquals(1, postsList.size());
        assertEquals("Post about Math! N2", postsList.get(0).content);
    }

    @Test
    public void testPaginatingRecommendedPostsFromThree() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 2, 1, 0);

        assertEquals(1, postsList.size());
        assertEquals("Post about Flowers! N3", postsList.get(0).content);
    }

    @Test
    public void testPaginatingRecommendedPostsFromFour() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 3, 1, 0);

        assertEquals(0, postsList.size());
    }

    @Test
    public void testPaginatingRecommendedPostsLimit() {
        User franky = User.findByLoginName("franky");

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 2, 0);

        assertEquals(2, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Math! N2", postsList.get(1).content);
    }

    @Test
    public void testPostsThatAreRatedByUserAreNotShown() {
        User franky = User.findByLoginName("franky");
        Post post = Post.find("byContent", "Post about Math! N2").first();
        PostRating postRating = new PostRating(post, franky, true);
        postRating.save();

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 0);

        assertEquals(2, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Flowers! N3", postsList.get(1).content);
    }

    @Test
    public void testPostsRatedByUsersWithUnknownSimilarityNotShown() {
        User franky = User.findByLoginName("franky");
        User billy = User.findByLoginName("billy");
        Similarity sim = Similarity.find("byTwo", billy).first();

        sim.delete();

        List<Post> postsList = postsService.getRecommendedPosts(franky, 0, 10, 0);

        assertEquals(2, postsList.size());
        assertEquals("Post about Money! N1", postsList.get(0).content);
        assertEquals("Post about Flowers! N3", postsList.get(1).content);
    }
}