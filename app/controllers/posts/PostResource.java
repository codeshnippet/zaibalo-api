package controllers.posts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Post;
import models.User;
import utils.HalGsonBuilder;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

import com.google.gson.reflect.TypeToken;

import controllers.comments.CommentResponse;
import controllers.rating.RatingResponse;
import controllers.users.UserResponse;

public class PostResource extends HalBaseResource {

	public long id;
	public String content;
	public UserResponse author;
	public long creationTimestamp;
	
	@HalEmbedded
	public List<CommentResponse> comments;
	
	public List<PostAttachmentResponse> attachments;
	public Set<RatingResponse> ratings;
	public int ratingSum;
	public int ratingCount;

	@HalLink(name = "delete")
	public String deleteLink;

	public static PostResource convertSinglePostResponse(Post post, User authUser) {
		PostResource postResponseJSON = new PostResource();
		postResponseJSON.id = post.id;
		postResponseJSON.content = post.content;
		postResponseJSON.creationTimestamp = post.creationDate.getTime();
		UserResponse userResponseJSON = UserResponse.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		postResponseJSON.comments = CommentResponse.convertToCommentResponsesList(post.comments, authUser);
		postResponseJSON.attachments = PostAttachmentResponse.convertToPostAttachmentListResponse(post.attachments);
		postResponseJSON.ratings = RatingResponse.convertToPostRatingListResponse(post.ratings);
		postResponseJSON.ratingCount = post.getRatingCount();
		postResponseJSON.ratingSum = post.getRatingSum();

		if (post.author.equals(authUser)) {
			postResponseJSON.deleteLink = "/posts/" + post.id;
		}

		postResponseJSON.setSelfRef("/posts/" + post.id);

		return postResponseJSON;
	}

}
