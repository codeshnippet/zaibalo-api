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
	public int commentsCount;
	
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
		postResponseJSON.commentsCount = post.comments == null ? 0 : post.comments.size();
		UserResponse userResponseJSON = UserResponse.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		return postResponseJSON;
	}

	public static SinglePostResponse convertToSinglePostResponse(Post post) {
		SinglePostResponse singlePostResponseJSON = new SinglePostResponse();
		singlePostResponseJSON.post = convertToPostResponse(post);
		singlePostResponseJSON.comments = post.comments == null ? null : CommentResponse.convertToCommentResponsesList(post.comments); 
		return singlePostResponseJSON;
	}
}
