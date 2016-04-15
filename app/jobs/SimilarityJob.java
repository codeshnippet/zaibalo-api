package jobs;

import models.Post;
import models.PostRating;
import models.Similarity;
import models.User;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import java.util.*;

@OnApplicationStart
public class SimilarityJob extends Job {

    public void doJob() {
        Logger.info("SimilarityJob started.");

        printRecommendedPosts();
        //populateSimilarity();

        Logger.info("SimilarityJob ended.");
    }

    private void populateSimilarity() {
        List<User> users = PostRating.getUserRatingPosts();
        Map<User, Set<PostRating>> map = PostRating.getUserPostRatingsMap();
        for(User one: users) {
            PriorityQueue<Critic> queue = new PriorityQueue<Critic>();
            for (User two : users) {
                if (one.id != two.id) {
                    Map<Post, Integer> ratingsOne = transform(map.get(one));
                    Map<Post, Integer> ratingsTwo = transform(map.get(two));

                    // ratingsOne now contains only the elements which are also contained in ratingsTwo.
                    ratingsOne.keySet().retainAll(ratingsTwo.keySet());

                    // do not save similarity value if less then 10 posts in common were rated
                    if (ratingsOne.keySet().size() < 5) {
                        continue;
                    }

                    double value = calculateSimilarity(ratingsOne, ratingsTwo);
                    queue.add(new Critic(two, value));
                }
            }

            for(Critic critic: queue) {
                saveSimilarity(one, critic.user, critic.value);
            }
        }
    }

    private Map<Post, Integer> transform(Set<PostRating> postRatings) {
        Map<Post, Integer> result = new HashMap<Post, Integer>();
        for(PostRating pr: postRatings){
            result.put(pr.post, pr.value);
        }
        return result;
    }

    class Critic implements Comparable<Critic>{
        public Critic(User user, Double value) {
            this.user = user;
            this.value = value;
        }

        public User user;
        public Double value;

        @Override
        public int compareTo(Critic o) {
            return this.value.compareTo(o.value);
        }
    }

    private void saveSimilarity(User one, User two, double value) {
        Similarity sim = Similarity.find("byOneAndTwo", one, two).first();
        if(sim == null){
            sim = new Similarity();
            sim.one = one;
            sim.two = two;
        }
        sim.value = value;
        sim.save();
    }

    private double calculateSimilarity(Map<Post, Integer> ratingsOne, Map<Post, Integer> ratingsTwo) {
        double sumOfSquares = 0;
        for(Post post: ratingsOne.keySet()){
            sumOfSquares += Math.pow(ratingsOne.get(post) - ratingsTwo.get(post), 2);
        }
        return 1/(1 + Math.sqrt(sumOfSquares));
    }


    public static void printRecommendedPosts(){
        User user = User.findById(1l);

        Map<User, Set<PostRating>> map = PostRating.getUserPostRatingsMap();

        Map<User, Double> similarities = Similarity.getSimilarities(user);
        List<Post> posts = PostRating.getRatesBySimilaritiesExceptUser(similarities.keySet(), user);

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
}