package controllers.posts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Post;
import models.User;
import utils.HalGsonBuilder;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

import com.google.gson.reflect.TypeToken;

import controllers.comments.CommentResponse;
import controllers.postratings.PostRatingResponse;
import controllers.users.UserResponse;

public class PostResponse extends HalBaseResource {

	public long id;
	public String content;
	public UserResponse author;
	public long creationTimestamp;
	public List<CommentResponse> comments;
	public List<PostAttachmentResponse> attachments;
	public Set<PostRatingResponse> ratings;
	public int ratingSum;
	public int ratingCount;

	@HalLink(name = "delete")
	public String deleteLink;

	public static String convertToPostResponsesList(List<Post> postsList, User user) {
		List<PostResponse> postJsonList = new ArrayList<PostResponse>(postsList.size());
		for (Post post : postsList) {
			PostResponse postResponseJSON = convertSinglePostResponse(post, user);
			postJsonList.add(postResponseJSON);
		}
		Type type = new TypeToken<List<HalResource>>() {
		}.getType();
		return new HalGsonBuilder().getGson().toJson(postJsonList, type);
	}

	public static String convertToPostResponse(Post post, User user) {
		PostResponse postResponseJSON = convertSinglePostResponse(post, user);
		return new HalGsonBuilder().getGson().toJson(postResponseJSON, HalResource.class);
	}

	private static PostResponse convertSinglePostResponse(Post post, User user) {
		PostResponse postResponseJSON = new PostResponse();
		postResponseJSON.id = post.id;
		postResponseJSON.content = post.content;
		postResponseJSON.creationTimestamp = post.creationDate.getTime();
		UserResponse userResponseJSON = UserResponse.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		postResponseJSON.comments = CommentResponse.convertToCommentResponsesList(post.comments);
		postResponseJSON.attachments = PostAttachmentResponse.convertToPostAttachmentListResponse(post.attachments);
		postResponseJSON.ratings = PostRatingResponse.convertToPostRatingListResponse(post.ratings);
		postResponseJSON.ratingCount = post.getRatingCount();
		postResponseJSON.ratingSum = post.getRatingSum();

		if (post.author.equals(user)) {
			postResponseJSON.deleteLink = "/posts/" + post.id;
		}

		postResponseJSON.setSelfRef("/posts/" + post.id);

		return postResponseJSON;
	}

}
