package controllers.comments;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.User;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;
import controllers.users.UserResource;

public class CommentResource implements HalResource {

	public long id;
	public String content;
	public UserResource author;
	public long creationTimestamp;

	@HalLink(name = "delete")
	public String deleteLink;
	
    @HalLink(name = "self")
    public String selfRef;
	
	public static List<CommentResource> convertToCommentResponsesList(List<Comment> commentsList, User authUser) {
		List<CommentResource> commentResponsesList = new ArrayList<CommentResource>();
		for(Comment comment: commentsList){
			commentResponsesList.add(convertToCommentResponse(comment, authUser));
		}
		return commentResponsesList;
	}

	public static CommentResource convertToCommentResponse(Comment comment, User authUser){
		CommentResource commentResponse = new CommentResource();
		commentResponse.id = comment.id;
		commentResponse.content = comment.content;
		commentResponse.author = UserResource.convertToJson(comment.author);
		commentResponse.creationTimestamp = comment.creationDate.getTime();
		
		if (comment.author.equals(authUser)) {
			commentResponse.deleteLink = "/comments/" + comment.id;
		}
		
		commentResponse.selfRef = "/comments/" + comment.id;
		
		return commentResponse;
	}
}
