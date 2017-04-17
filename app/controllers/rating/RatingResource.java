package controllers.rating;

import java.util.HashSet;
import java.util.Set;

import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import models.Post;
import models.PostRating;
import controllers.users.UserResource;
import models.Ratable;
import models.Rating;

public class RatingResource extends HalBaseResource {

	public long creationTimestamp;
	public String value;

    @HalEmbedded
	public UserResource user;

	public RatingResource(Rating rating){
		this.creationTimestamp = rating.creationDate.getTime();
		this.user = UserResource.convertToJson(rating.user);
		this.value = rating.isPositive() ? "+1" : "-1";
	}

}
