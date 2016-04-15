package controllers.posts.service;

import models.Post;
import models.User;

import java.util.List;

public interface PostsService {
    public List<Post> getRecommendedPosts(User user);
}
