package controllers.comments;

import models.Comment;
import models.Post;
import play.mvc.Controller;

public class Comments extends Controller {
	
    public static void getPostComments(long postId) {
    	response.setContentTypeIfNotSet("application/json");
    	
    	Post post = Post.findById(postId);
    	
    	renderJSON(CommentResponse.convertToCommentResponsesList(post.comments));
    }
    
    public static void createPostComment(long postId) {
    	//TODO: Implement service
    	ok();
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
