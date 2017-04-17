package controllers.rating;

import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;
import controllers.security.Security;
import models.CommentRating;
import models.Ratable;
import models.Rating;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by acidum on 4/13/17.
 */
public class RatingResourceList extends HalBaseResource {

    @HalEmbedded
    public List<RatingResource> ratings = new ArrayList<RatingResource>();

    public int ratingSum = 0;
    public int ratingCount = 0;

    @HalLink(name = "ratePostUp")
    public String ratePostUp;

    @HalLink(name = "ratePostDown")
    public String ratePostDown;

    public static RatingResourceList convertToRatingResourceList(Ratable ratable) {
        RatingResourceList resourceList = new RatingResourceList();
        for(Rating rating: ratable.getRatings()){
            resourceList.ratings.add(new RatingResource(rating));
        }
        resourceList.ratingCount = ratable.getRatings().size();
        resourceList.ratingSum = getRatingSum(ratable.getRatings());

        User authUser = Security.getAuthenticatedUser();

        boolean isPositive = true;
        boolean hasPositiveRating = ratable.hasRating(authUser, isPositive);
        if(authUser != null && !hasPositiveRating) {
            resourceList.ratePostUp = ratable.getRateUrl();
        }

        isPositive = false;
        boolean hasNegativeRating = ratable.hasRating(authUser, isPositive);
        if(authUser != null && !hasNegativeRating) {
            resourceList.ratePostDown = ratable.getRateUrl();
        }

        return resourceList;
    }

    private static Integer getRatingSum(List<? extends Rating> ratingList) {
        int sum = 0;
        for (Rating rating : ratingList) {
            if (rating.isPositive()) {
                sum++;
            } else {
                sum--;
            }
        }
        return sum;
    }

}
