package jobs;

import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.FunctionalTest;

/**
 * Created by acidum on 4/11/16.
 */
public class SimilarityJobTest extends FunctionalTest {

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testBasicSimilarityCalculation(){
        Fixtures.loadModels("data/post-ratings.yml");

        new SimilarityJob().doJob();
    }
}
