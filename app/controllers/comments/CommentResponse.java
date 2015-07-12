package controllers.comments;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.User;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalLink;
import controllers.users.UserResponse;

public class CommentResponse extends HalBaseResource {

	public long id;
	public String content;
	public UserResponse author;
	public long creationTimestamp;
	public int ratingSum;
	public int ratingCount;

	@HalLink(name = "delete")
	public String deleteLink;
	
	public static List<CommentResponse> convertToCommentResponsesList(List<Comment> commentsList, User authUser) {
		List<CommentResponse> commentResponsesList = new ArrayList<CommentResponse>();
		for(Comment comment: commentsList){
			commentResponsesList.add(convertToCommentResponse(comment, authUser));
		}
		return commentResponsesList;
	}

	public static CommentResponse convertToCommentResponse(Comment comment, User authUser){
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.id = comment.id;
		commentResponse.content = comment.content;
		commentResponse.author = UserResponse.convertToJson(comment.author);
		commentResponse.creationTimestamp = comment.creationDate.getTime();
		commentResponse.ratingCount = comment.getRatingCount();
		commentResponse.ratingSum = comment.getRatingSum();
		
		if (comment.author.equals(authUser)) {
			commentResponse.deleteLink = "/comments/" + comment.id;
		}
		
		commentResponse.setSelfRef("/comments/" + comment.id);
		
		return commentResponse;
	}
}
