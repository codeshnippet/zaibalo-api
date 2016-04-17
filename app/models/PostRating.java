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

    public static Map<User, Map<Post, Integer>> getPostRatingsFromUsers(Set<User> users){
        Map<User, Map<Post, Integer>> result = new HashMap<User, Map<Post, Integer>>();
        List<PostRating> list = PostRating.find("user in :users").setParameter("users", users).fetch();
        for(PostRating rating: list){
            if(result.get(rating.user) == null){
                result.put(rating.user, new HashMap<Post, Integer>());
            }
            result.get(rating.user).put(rating.post, rating.value);
        }
        return result;
    }

    public static List<Post> getRatesBySimilaritiesExceptUser(Set<User> similarities, User user){
        return PostRating.find("select pr.post from PostRating as pr " +
                "where pr.user in :similarities and pr.user != :user " +
                "group by pr.post having count(pr.user) >= :threshold")
                .setParameter("user", user)
                .setParameter("similarities", similarities)
                .setParameter("threshold", Math.round(similarities.size() * 0.2))
                .fetch();
    }

    public static List<User> getUserRatingPosts() {
        return PostRating.find("select pr.user from PostRating as pr group by pr.user").fetch();
    }
}
