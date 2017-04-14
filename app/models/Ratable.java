package models;

import play.db.jpa.GenericModel;

import java.util.List;

public interface Ratable {

	boolean hasRating(User user, boolean isPositive);
	Rating getRating(User user, boolean isPositive);
	Rating createRating(User user, boolean isPositive);
    List<? extends Rating> getRatings();
}
