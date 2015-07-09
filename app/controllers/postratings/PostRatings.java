package controllers.postratings;

import java.io.InputStreamReader;

import models.Post;
import models.PostRating;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.mvc.Http.Header;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class PostRatings extends BasicController{

    @Secured
    public static void createPostRating(long postId) {
        User user = Security.getAuthenticatedUser();
        Post post = Post.findById(postId);
        PostRatingRequest postRatingJSON = new GsonBuilder().create().fromJson(new InputStreamReader(request.body),
            PostRatingRequest.class);

        boolean voteExists = PostRating.hasPostRating(post, user, postRatingJSON.isPositive);
        if(voteExists){
            writeToResponseBody("POST_RATE_EXISTS");
            badRequest();
        }

        boolean opositeVoteExists = PostRating.hasPostRating(post, user, !postRatingJSON.isPositive);
        if(opositeVoteExists){
            PostRating.getPostRating(post, user, !postRatingJSON.isPositive).delete();
            response.status = 204;
            renderText(StringUtils.EMPTY);
        }

        PostRating postRating = new PostRating(post, user, postRatingJSON.isPositive).save();
        
        String location = request.host + "/post-ratings/" + postRating.id;
        response.headers.put("Location", new Header("Location", location));
        response.status = 201;
        renderJSON(StringUtils.EMPTY);
    }

}
