package controllers.rating;

import java.util.HashSet;
import java.util.Set;

import models.Post;
import models.PostRating;
import controllers.users.UserResponse;

public class RatingResponse {

	public long creationTimestamp;
	public UserResponse user;
	public String value;
	
	public RatingResponse(PostRating postRating){
		this.creationTimestamp = postRating.creationDate.getTime();
		this.user = UserResponse.convertToJson(postRating.user);
		this.value = postRating.isPositive() ? "+1" : "-1";
	}
	
	public static Set<RatingResponse> convertToPostRatingListResponse(Set<PostRating> ratings) {
		Set<RatingResponse> postRatings = new HashSet<RatingResponse>();
		for(PostRating postRating: ratings){
			postRatings.add(new RatingResponse(postRating));
		}
		return postRatings;
	}
}
