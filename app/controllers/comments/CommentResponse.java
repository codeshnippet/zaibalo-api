package controllers.comments;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import controllers.users.UserResponse;

public class CommentResponse {

	public long id;
	public String content;
	public UserResponse author;
	public long creationTimestamp;
	
	public static List<CommentResponse> convertToCommentResponsesList(List<Comment> commentsList) {
		List<CommentResponse> commentResponsesList = new ArrayList<CommentResponse>();
		for(Comment comment: commentsList){
			commentResponsesList.add(convertToCommentResponse(comment));
		}
		return commentResponsesList;
	}
	
	public static CommentResponse convertToCommentResponse(Comment comment){
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.id = comment.id;
		commentResponse.content = comment.content;
		commentResponse.author = UserResponse.convertToJson(comment.author);
		commentResponse.creationTimestamp = comment.creationDate.getTime();
		return commentResponse;
	}
}
