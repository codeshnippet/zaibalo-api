package controllers.rating;

public class RatingRequest {

    public RatingRequest(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public boolean isPositive;
}
