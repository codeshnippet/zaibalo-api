package controllers.rating;

import java.io.InputStreamReader;

import models.Ratable;
import models.Rating;
import models.User;

import play.mvc.Util;

import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.security.Security;

import static controllers.rating.RatingResourceList.convertToRatingResourceList;

public abstract class Ratings extends BasicController {
	
	@Util
	protected static void createRating(Ratable ratable) {
    	User user = Security.getAuthenticatedUser();
    	RatingRequest ratingJSON = new GsonBuilder().create().fromJson(new InputStreamReader(request.body),
                RatingRequest.class);

    	boolean voteExists = ratable.hasRating(user, ratingJSON.isPositive);
    	
        if(voteExists){
            writeToResponseBody("POST_RATE_EXISTS");
            badRequest();
        }
        
        Rating oppositeRating = ratable.getRating(user, !ratingJSON.isPositive);
        
        if(oppositeRating != null){
        	oppositeRating.delete();
            response.status = 200;
            renderJSON(convertToHalResponse(convertToRatingResourceList(ratable)));
        }

        ratable.createRating(user, ratingJSON.isPositive).save();

        response.status = 201;
        renderJSON(convertToHalResponse(convertToRatingResourceList(ratable)));
	}

}
