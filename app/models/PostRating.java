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

    public static List<Post> getNotRatedUserPosts(User user){
        return PostRating.find("select pr.post from PostRating as pr where pr.user != ? group by pr.post", user).fetch();
    }

    public static Integer getUserRatingForPost(User user, Post post){
        return PostRating.find("select pr.value from PostRating as pr where pr.user = ? and pr.post = ?", user, post)
                .first();
    }

    public static void printRecommendedPosts(Map<User, Set<PostRating>> map){
        User user = User.findById(1l);

        Map<User, Double> similarities = Similarity.getSimilarities(user);
        List<Post> posts = PostRating.getNotRatedUserPosts(user);

        Map<Post, Double> recommendations = new HashMap<Post, Double>();

        for(Post post: posts) {
            double simSum = 0;
            double sum = 0;
            for(User critic: similarities.keySet()){
                Double similarity = similarities.get(critic);
                Set<PostRating> ratingSet = map.get(critic);

                Integer value = null;
                for(PostRating pr: ratingSet){
                    if(pr.post.id == post.id){
                        value = pr.value;
                        break;
                    }
                }

                if(value != null){
                    simSum += similarity;
                    sum += similarity * value;
                }
            }

            if(sum == 0){
                continue;
            }

            double result = sum / simSum;
            recommendations.put(post, result);
        }

        for(Post post: recommendations.keySet()){
            System.out.println("Post id:" + post.id + " rating:" + recommendations.get(post));
        }
    }

    public static List<User> getUserRatingPosts() {
        return PostRating.find("select pr.user from PostRating as pr group by pr.user").fetch();
    }
}
