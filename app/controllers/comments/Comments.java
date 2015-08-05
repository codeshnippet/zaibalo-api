package controllers.comments;

import java.io.InputStreamReader;

import models.Comment;
import models.Post;
import models.User;
import play.mvc.Http.Header;
import play.mvc.With;
import ch.halarious.core.HalResource;

import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Comments extends BasicController {

    public static void getPostComments(long postId) {
    	User user = Security.getAuthenticatedUser();
    	
    	response.setContentTypeIfNotSet("application/json");

    	Post post = Post.findById(postId);

    	renderJSON(CommentResource.convertToCommentResponsesList(post.comments, user));
    }

	@Secured
    public static void createPostComment(long postId) {
    	User authUser = Security.getAuthenticatedUser();
    	Post post = Post.findById(postId);

		CommentRequest commentJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), CommentRequest.class);
		
		if(!commentJSON.isValid()){
			badRequest();
		}
		
		Comment comment = new Comment();
		comment.author = authUser;
		comment.content = commentJSON.content;
		comment.post = post;
		comment.save();

		String location = request.host + "/comments/" + comment.id;
		response.headers.put("Location", new Header("Location", location));
		response.status = 201;
		
		HalResource commentResponse = CommentResource.convertToCommentResponse(comment, authUser);
		renderJSON(convertToHalResponse(commentResponse));
    }

    public static void getComment(long id) {
    	//TODO: Implement service
    	ok();
    }

    public static void editComment(long id) {
    	//TODO: Implement service
    	ok();
    }

    @Secured
    public static void deleteComment(long id) {
    	Comment comment = Comment.findById(id);
    	if(comment == null){
    		notFound();
    	}
    	
    	Security.verifyOwner(comment.author);
    	
    	comment.delete();
    	
    	ok();
    }
}
