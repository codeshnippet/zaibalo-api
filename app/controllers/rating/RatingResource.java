package controllers.rating;

import java.util.HashSet;
import java.util.Set;

import models.Post;
import models.PostRating;
import controllers.users.UserResource;

public class RatingResource {

	public long creationTimestamp;
	public UserResource user;
	public String value;
	
	public RatingResource(PostRating postRating){
		this.creationTimestamp = postRating.creationDate.getTime();
		this.user = UserResource.convertToJson(postRating.user);
		this.value = postRating.isPositive() ? "+1" : "-1";
	}
	
	public static Set<RatingResource> convertToPostRatingListResponse(Set<PostRating> ratings) {
		Set<RatingResource> postRatings = new HashSet<RatingResource>();
		for(PostRating postRating: ratings){
			postRatings.add(new RatingResource(postRating));
		}
		return postRatings;
	}
}
