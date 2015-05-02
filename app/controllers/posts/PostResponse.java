package controllers.posts;

import java.util.ArrayList;
import java.util.List;

import controllers.comments.CommentResponse;
import controllers.users.UserResponse;
import models.Comment;
import models.Post;

public class PostResponse {

	public long id;
	public String content;
	public UserResponse author;
	public long creationTimestamp;
	public List<CommentResponse> comments;
	public List<PostAttachmentResponse> attachments;
	
	public static List<PostResponse> convertToPostResponsesList(List<Post> postsList) {
		List<PostResponse> postJsonList = new ArrayList<PostResponse>(postsList.size());
		for(Post post: postsList){
			PostResponse postResponseJSON = convertToPostResponse(post);
			postJsonList.add(postResponseJSON);
		}
		return postJsonList;
	}

	public static PostResponse convertToPostResponse(Post post) {
		PostResponse postResponseJSON = new PostResponse();
		postResponseJSON.id = post.id;
		postResponseJSON.content = post.content;
		postResponseJSON.creationTimestamp = post.creationDate.getTime();
		UserResponse userResponseJSON = UserResponse.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		postResponseJSON.comments = CommentResponse.convertToCommentResponsesList(post.comments);
		postResponseJSON.attachments = PostAttachmentResponse.convertToPostAttachmentListResponse(post.attachments);
		return postResponseJSON;
	}

}
