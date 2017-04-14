package controllers.posts;

import java.util.List;
import java.util.Set;

import controllers.rating.RatingResourceList;
import models.Post;
import models.User;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;

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
	public RatingResourceList ratings;

	@HalLink(name = "delete")
	public String deleteLink;
	
	@HalLink(name = "addComment")
	public String addCommentLink;

    @HalLink(name = "ratePost")
    public String ratePost;

	public static PostResource convertSinglePostResponse(Post post, User authUser) {
		PostResource postResponseJSON = new PostResource();
		postResponseJSON.id = post.id;
		postResponseJSON.content = post.content;
		postResponseJSON.creationTimestamp = post.creationDate.getTime();
		UserResource userResponseJSON = UserResource.convertToJson(post.author);
		postResponseJSON.author = userResponseJSON;
		postResponseJSON.comments = CommentResource.convertToCommentResponsesList(post.comments, authUser);
		postResponseJSON.attachments = PostAttachmentResponse.convertToPostAttachmentListResponse(post.attachments);
		postResponseJSON.ratings = RatingResourceList.convertToRatingResourceList(post);

		if(authUser != null){
			postResponseJSON.addCommentLink = "/posts/" + post.id + "/comments";
		}
		
		if (post.author.equals(authUser)) {
			postResponseJSON.deleteLink = "/posts/" + post.id;
		}

        postResponseJSON.ratePost = "/posts/" + post.id + "/post-ratings";

		postResponseJSON.setSelfRef("/posts/" + post.id);

		return postResponseJSON;
	}

}
