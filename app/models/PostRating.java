package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity
@Table(name="post_rating")
public class PostRating extends Rating {
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", referencedColumnName="id")
    public Post post;
    
    public PostRating(Post post, User user, boolean isPositive) {
        super(user, isPositive);
        this.post = post;
    }

    public static boolean hasPostRating(Post post, User user, boolean isPositive) {
        return getPostRating(post, user, isPositive) != null;
    }

    public static PostRating getPostRating(Post post, User user, boolean isPositive) {
        return PostRating.find("byPostAndUserAndValue", post, user, isPositive ? 1 : -1).first();
    }

    public static Map<User, Set<PostRating>> getUserPostRatingsMap(){
        Map<User, Set<PostRating>> result = new HashMap<User, Set<PostRating>>();
        List<PostRating> list = PostRating.all().fetch();
        for(PostRating rating: list){
            if(result.get(rating.user) == null){
                result.put(rating.user, new HashSet<PostRating>());
            }
            result.get(rating.user).add(rating);
        }
        return result;
    }

    public static List<User> getUserRatingPosts() {
        return PostRating.find("select pr.user from PostRating as pr group by pr.user").fetch();
    }


    public static long getMaxRecoThreshold(User user) {
        String query = "select count(pr.user) from PostRating pr, Similarity sim " +
                "where pr.user = sim.two " +
                "and sim.one = :user " +
                "and pr.user != :user " +
                "group by pr.post " +
                "order by count(pr.user) desc";

        List<Long> countList = PostRating.find(query).setParameter("user", user).fetch(1);
        return countList.isEmpty() ? 0 : countList.get(0);
    }
}
