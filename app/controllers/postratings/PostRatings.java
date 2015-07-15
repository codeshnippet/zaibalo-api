package controllers.postratings;

import models.Post;
import play.mvc.With;
import controllers.rating.Ratings;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class PostRatings extends Ratings {

    @Secured
    public static void createPostRating(long postId) {
    	Post post = Post.findById(postId);
    	createRating(post);
    }

}
