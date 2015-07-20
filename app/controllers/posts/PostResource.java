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

import controllers.comments.CommentResource;
import controllers.rating.RatingResource;
import controllers.users.UserResource;

public class PostResource extends HalBaseResource {

	public long id;
	public String content;
	public UserResource author;
	public long creationTimestamp;
	
	@HalEmbedded
	public List<CommentResource> comments;
	
	public List<PostAttachmentResponse> attachments;
	public Set<RatingResource> ratings;
	public int ratingSum;
	public int ratingCount;

	@HalLink(name = "delete")
	public String deleteLink;

	public static PostResource convertSinglePostResponse(Post post, User authUser) {
		PostResource postResponseJSON = new PostResource();
		postResponseJSON.id = post.id;
		postResponseJSON.content = post.content;
		postResponseJSON.creationTimestamp = post.creationDate.getTime();
		UserResource userResponseJSON = UserResource.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		postResponseJSON.comments = CommentResource.convertToCommentResponsesList(post.comments, authUser);
		postResponseJSON.attachments = PostAttachmentResponse.convertToPostAttachmentListResponse(post.attachments);
		postResponseJSON.ratings = RatingResource.convertToPostRatingListResponse(post.ratings);
		postResponseJSON.ratingCount = post.getRatingCount();
		postResponseJSON.ratingSum = post.getRatingSum();

		if (post.author.equals(authUser)) {
			postResponseJSON.deleteLink = "/posts/" + post.id;
		}

		postResponseJSON.setSelfRef("/posts/" + post.id);

		return postResponseJSON;
	}

}
