package controllers.posts.service;

import models.Post;
import models.User;

import java.util.List;
import java.util.Map;

public interface PostsService {

    List<Post> getRecommendedPosts(User user, int from, int limit);

    long getRecommendedPostsCount(User user);

    List<Post> getPostsByTag(String tag, Post.SortBy sortBy, int from, int limit);

    List<Post> getLatestPosts(Post.SortBy creationDate, int from, int limit);

    List<Post> getUserPosts(User user, Post.SortBy creationDate, int from, int limit);
}
