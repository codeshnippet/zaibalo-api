package controllers.posts.service.impl;

import controllers.posts.service.PostsService;
import models.Post;
import models.PostRating;
import models.Similarity;
import models.User;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PostsServiceImpl implements PostsService {

    @Override
    public List<Post> getRecommendedPosts(User user) {
        Map<User, Set<PostRating>> map = PostRating.getUserPostRatingsMap();

        Map<User, Double> similarities = Similarity.getSimilarities(user);
        List<Post> posts = PostRating.getRatesBySimilaritiesExceptUser(similarities.keySet(), user);

        List<Map.Entry<Post, Double>> recommendations = new ArrayList<Map.Entry<Post, Double>>();

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

            double value = sum / simSum;
            recommendations.add(new AbstractMap.SimpleEntry<Post, Double>(post, value));
        }

        Collections.sort(recommendations,
                new Comparator<Map.Entry<Post, Double>>() {
                    @Override
                    public int compare(Map.Entry<Post, Double> e1, Map.Entry<Post, Double> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        List<Post> result = new ArrayList<Post>(recommendations.size());
        for(Map.Entry<Post, Double> entry: recommendations){
            result.add(entry.getKey());
        }

        return result;
    }


}
