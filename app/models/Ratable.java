package models;

import play.db.jpa.GenericModel;

public interface Ratable {

	boolean hasRating(User user, boolean isPositive);
	Rating getRating(User user, boolean isPositive);
	Rating createRating(User user, boolean isPositive);
}
