package controllers.rating;

import models.CommentRating;
import models.Ratable;
import models.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by acidum on 4/13/17.
 */
public class RatingResourceList {

    public List<RatingResource> ratings = new ArrayList<RatingResource>();
    public int ratingSum = 0;
    public int ratingCount = 0;

    public static RatingResourceList convertToRatingResourceList(Ratable ratable) {
        RatingResourceList resourceList = new RatingResourceList();
        for(Rating rating: ratable.getRatings()){
            resourceList.ratings.add(new RatingResource(rating));
        }
        resourceList.ratingCount = ratable.getRatings().size();
        resourceList.ratingSum = getRatingSum(ratable.getRatings());
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
