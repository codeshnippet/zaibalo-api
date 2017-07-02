package jobs;

import models.Post;
import models.PostRating;
import models.Similarity;
import models.User;
import play.Logger;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;

import java.util.*;

@On("0 0 3 * * ?")
public class SimilarityJob extends Job {

    private static class Statistics {
        public int processedCount = 0;
    }

    public void doJob() {
        Logger.info("SimilarityJob started.");

        Statistics statistics = populateSimilarity();

        Logger.info("SimilarityJob ended. Processed items count %d", statistics.processedCount);
    }

    private Statistics populateSimilarity() {
        Statistics stat = new Statistics();

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

                    // do not save similarity value if less then 5 posts in common were rated
                    if (ratingsOne.keySet().size() < 5) {
                        continue;
                    }

                    double value = calculateSimilarity(ratingsOne, ratingsTwo);
                    queue.add(new Critic(two, value));
                }
            }

            for(Critic critic: queue) {
                saveSimilarity(one, critic.user, critic.value);
                stat.processedCount++;
            }
        }
        return stat;
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
}