package controllers.posts.service;

import models.Post;
import models.User;

import java.util.List;

public interface PostsService {

    List<Post> getRecommendedPosts(User user, int from, int limit, long threshold);

    long getRecommendedPostsCount(User user, long threshold);

    List<Post> getPostsByTag(String tag, Post.SortBy sortBy, int from, int limit);

    List<Post> getLatestPosts(int from, int limit);

    List<Post> getUserPosts(User user, Post.SortBy creationDate, int from, int limit);
}
