package models;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class PostRatingTest extends UnitTest{
    
    @Before
    public void deleteModels() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testGetPostRating(){
        Fixtures.loadModels("data/post-rating.yml");
        PostRating postRating = (PostRating) PostRating.findAll().get(0);

        assertNotNull(postRating.creationDate);
        assertNotNull(postRating.post);
        assertNotNull(postRating.user);
        assertEquals(-1, postRating.value);
    }

}
