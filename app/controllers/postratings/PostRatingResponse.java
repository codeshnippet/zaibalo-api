package controllers.postratings;

import java.util.HashSet;
import java.util.Set;

import models.Post;
import models.PostRating;
import controllers.users.UserResponse;

public class PostRatingResponse {

	public long creationTimestamp;
	public UserResponse user;
	public String value;
	
	public PostRatingResponse(PostRating postRating){
		this.creationTimestamp = postRating.creationDate.getTime();
		this.user = UserResponse.convertToJson(postRating.user);
		this.value = postRating.isPositive() ? "+1" : "-1";
	}
	
	public static Set<PostRatingResponse> convertToPostRatingListResponse(Set<PostRating> ratings) {
		Set<PostRatingResponse> postRatings = new HashSet<PostRatingResponse>();
		for(PostRating postRating: ratings){
			postRatings.add(new PostRatingResponse(postRating));
		}
		return postRatings;
	}
}
