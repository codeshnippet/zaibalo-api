package models;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CommentRatingTest extends UnitTest {

	@Before
	public void deleteModels() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetCommentRating() {
		Fixtures.loadModels("data/comment-rating.yml");
		CommentRating commentRating = (CommentRating) CommentRating.findAll().get(0);

		assertNotNull(commentRating.creationDate);
		assertNotNull(commentRating.comment);
		assertNotNull(commentRating.user);
		assertEquals(-1, commentRating.value);
	}
}
