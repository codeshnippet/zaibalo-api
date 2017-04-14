package controllers.rating;

import java.util.HashSet;
import java.util.Set;

import models.Post;
import models.PostRating;
import controllers.users.UserResource;
import models.Ratable;
import models.Rating;

public class RatingResource {

	public long creationTimestamp;
	public UserResource user;
	public String value;
	
	public RatingResource(Rating rating){
		this.creationTimestamp = rating.creationDate.getTime();
		this.user = UserResource.convertToJson(rating.user);
		this.value = rating.isPositive() ? "+1" : "-1";
	}

}
