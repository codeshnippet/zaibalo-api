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
                "select pr.post from PostRating as pr, Similarity sim " + //select posts from post ratings
                        "where pr.user = sim.two " + // where post rating was left by users
                        "and pr.post not in (select p.post from PostRating p where p.user = :user) " +    // that are not the current one
                        "and sim.one = :user " +     // but which have similarity with current one
                        "group by pr.post " +
                        "having count(pr) >= :threshold " +
                        "order by sum(pr.value * sim.value) / count(pr) desc";

        return Post.find(query)
                .setParameter("user", user)
                .setParameter("threshold", threshold)
                .from(from)
                .fetch(limit);
    }

    @Override
    public long getRecommendedPostsCount(User user, long threshold) {
        String query =
                "select pr.post from PostRating as pr, Similarity sim " + //select posts from post ratings
                        "where pr.user = sim.two " + // where post rating was left by users
                        "and pr.post not in (select p.post from PostRating p where p.user = :user) " +    // that are not the current one
                        "and sim.one = :user " +     // but which have similarity with current one
                        "group by pr.post " +
                        "having count(pr) >= :threshold " +
                        "order by sum(pr.value * sim.value) / count(pr) desc";

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
