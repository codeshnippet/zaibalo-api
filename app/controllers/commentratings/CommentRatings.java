package controllers.commentratings;

import models.Comment;
import play.mvc.With;
import controllers.rating.Ratings;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class CommentRatings extends Ratings {

	@Secured
	public static void createCommentRating(long commentId) {
		Comment comment = Comment.findById(commentId);
		createRating(comment);
	}

}
