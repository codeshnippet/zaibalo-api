package controllers.comments;

import java.io.InputStreamReader;

import models.Comment;
import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Comments extends Controller {

    public static void getPostComments(long postId) {
    	response.setContentTypeIfNotSet("application/json");

    	Post post = Post.findById(postId);

    	renderJSON(CommentResponse.convertToCommentResponsesList(post.comments));
    }

	@Secured
    public static void createPostComment(long postId) {
    	User connected = Security.getAuthenticatedUser();
    	Post post = Post.findById(postId);

		CommentRequest commentJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), CommentRequest.class);
		Comment comment = new Comment();
		comment.author = connected;
		comment.content = commentJSON.content;
		comment.post = post;
		comment.save();

		String location = request.host + "/comments/" + comment.id;
		response.headers.put("Location", new Header("Location", location));
		response.status = 201;
		renderJSON(CommentResponse.convertToCommentResponse(comment));
    }

    public static void getComment(long id) {
    	//TODO: Implement service
    	ok();
    }

    public static void editComment(long id) {
    	//TODO: Implement service
    	ok();
    }

    public static void deleteComment(long id) {
    	//TODO: Implement service
    	ok();
    }
}
