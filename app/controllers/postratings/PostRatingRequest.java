package controllers.postratings;

public class PostRatingRequest {

    public PostRatingRequest(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public boolean isPositive;
}
