package controllers.posts;

import java.util.List;

import controllers.comments.CommentResponse;
import controllers.users.UserResponse;

public class SinglePostResponse {

	public PostResponse post;
	public List<CommentResponse> comments;
	
}
