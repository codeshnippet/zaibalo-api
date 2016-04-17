package controllers.posts.service.impl;

import controllers.posts.service.PostsService;
import models.Post;
import models.PostRating;
import models.Similarity;
import models.User;
import play.db.jpa.GenericModel;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PostsServiceImpl implements PostsService {

    public List<Map.Entry<Post, Double>> getAllRecommendedPosts(User user) {
        Map<User, Double> similarities = Similarity.getSimilarities(user);
        Map<User, Map<Post, Integer>> map = PostRating.getPostRatingsFromUsers(similarities.keySet());
        List<Post> posts = PostRating.getRatesBySimilaritiesExceptUser(similarities.keySet(), user);

        List<Map.Entry<Post, Double>> recommendations = new ArrayList<Map.Entry<Post, Double>>();

        for(Post post: posts) {
            double simSum = 0;
            double sum = 0;
            for(User critic: similarities.keySet()){
                Double similarity = similarities.get(critic);
                Map<Post, Integer> ratingSet = map.get(critic);

                Integer value = ratingSet.get(post);

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

        return recommendations;
    }

    @Override
    public List<Post> getPostsByTag(String tag, Post.SortBy sortBy, int from, int limit) {
        String query = "content like :tag";
        if(sortBy.equals(Post.SortBy.CREATION_DATE)){
            query += " order by creationDate desc";
        }

        return Post.find(query).setParameter("tag", "%#" + tag + "%").from(from).fetch(limit);
    }

    @Override
    public List<Post> getLatestPosts(Post.SortBy sortBy, int from, int limit) {
        String query = "";
        if(sortBy.equals(Post.SortBy.CREATION_DATE)){
            query += " order by creationDate desc";
        }

        return Post.find(query).from(from).fetch(limit);
    }

    @Override
    public List<Post> getUserPosts(User user, Post.SortBy sortBy, int from, int limit) {
        String query = "author = :author";
        if(sortBy.equals(Post.SortBy.CREATION_DATE)){
            query += " order by creationDate desc";
        }

        return Post.find(query).setParameter("author", user).from(from).fetch(limit);
    }

}
