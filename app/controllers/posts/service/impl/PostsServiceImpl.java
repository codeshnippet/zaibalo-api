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

    @Override
    public List<Post> getRecommendedPosts(User user, int from, int limit, long threshold) {
        String query =
                "select pr.post from PostRating as pr, Similarity sim " +
                        "where pr.user = sim.two " +
                        "and pr.user != :user " +
                        "and sim.one = :user " +
                        "group by pr.post " +
                        "having count(pr.user) >= :threshold " +
                        "order by sum(pr.value * sim.value) / sum(sim.value) desc";

        return PostRating.find(query).setParameter("user", user).setParameter("threshold", threshold).from(from).fetch(limit);
    }

    @Override
    public long getRecommendedPostsCount(User user, long threshold) {
        String query =
                "select pr.post from PostRating as pr, Similarity sim " +
                        "where pr.user = sim.two " +
                        "and pr.user != :user " +
                        "and sim.one = :user " +
                        "group by pr.post " +
                        "having count(pr.user) >= :threshold " +
                        "order by sum(pr.value * sim.value) / sum(sim.value) desc";

        return PostRating.find(query).setParameter("user", user).setParameter("threshold", threshold).fetch().size();
    }

    @Override
    public List<Post> getPostsByTag(String tag, Post.SortBy sortBy, int from, int limit) {
        String query = "content like :tag";
        if (sortBy.equals(Post.SortBy.CREATION_DATE)) {
            query += " order by creationDate desc";
        }

        return Post.find(query).setParameter("tag", "%#" + tag + "%").from(from).fetch(limit);
    }

    @Override
    public List<Post> getLatestPosts(int from, int limit) {
        return Post.find(" order by creationDate desc").from(from).fetch(limit);
    }

    @Override
    public List<Post> getUserPosts(User user, Post.SortBy sortBy, int from, int limit) {
        String query = "author = :author";
        if (sortBy.equals(Post.SortBy.CREATION_DATE)) {
            query += " order by creationDate desc";
        }

        return Post.find(query).setParameter("author", user).from(from).fetch(limit);
    }

}
