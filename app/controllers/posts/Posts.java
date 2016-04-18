package controllers.posts;

import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalResource;
import com.google.common.base.Optional;
import com.google.gson.GsonBuilder;
import controllers.BasicController;
import controllers.posts.service.PostsService;
import controllers.posts.service.impl.PostsServiceImpl;
import controllers.security.Secured;
import controllers.security.Security;
import models.Post;
import models.PostRating;
import models.User;
import play.mvc.Http.Header;
import play.mvc.Router;
import play.mvc.Util;
import play.mvc.With;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@With(Security.class)
public class Posts extends BasicController {

    private static PostsService postsService = new PostsServiceImpl();

    public static void getPostsByTag(String tag, int from, int limit) {
        from = (from == 0) ? 0 : from;
        limit = (limit == 0) ? 10 : limit;

        List<Post> postsList = postsService.getPostsByTag(tag, Post.SortBy.CREATION_DATE, from, limit);

        long count = getPostsByTagCount(tag);

        String nextUrl = null;
        if (postsList.size() <= count) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("from", from + 10);
            map.put("tag", tag);
            nextUrl = Router.reverse("posts.Posts.getPostsByTag", map).url;
        }
        Optional<String> next = Optional.fromNullable(nextUrl);

        User user = Security.getAuthenticatedUser();
        String addPostUrl = null;
        if (user != null) {
            addPostUrl = Router.reverse("posts.Posts.createPost").url;
        }
        Optional<String> addPost = Optional.fromNullable(addPostUrl);

        renderPostsListJson(postsList, addPost, next);
    }

    public static void getPosts(int from, int limit) {
        from = (from == 0) ? 0 : from;
        limit = (limit == 0) ? 10 : limit;

        List<Post> postsList = postsService.getLatestPosts(from, limit);

        long count = getPostsCount();

        String nextUrl = null;
        if (postsList.size() <= count) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("from", from + 10);
            nextUrl = Router.reverse("posts.Posts.getPosts", map).url;
        }
        Optional<String> next = Optional.fromNullable(nextUrl);

        User user = Security.getAuthenticatedUser();
        String addPostUrl = null;
        if (user != null) {
            addPostUrl = Router.reverse("posts.Posts.createPost").url;
        }
        Optional<String> addPost = Optional.fromNullable(addPostUrl);

        renderPostsListJson(postsList, addPost, next);
    }

    public static void getRecommendedPosts(int from, int limit) {
        from = (from == 0) ? 0 : from;
        limit = (limit == 0) ? 10 : limit;

        response.setContentTypeIfNotSet("application/json");

        User user = Security.getAuthenticatedUser();

        List<Post> postsList = postsService.getRecommendedPosts(user, from, limit);

        long count = postsService.getRecommendedPostsCount(user);

        String nextUrl = null;
        if (from + postsList.size() < count) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("from", from + 10);
            nextUrl = Router.reverse("posts.Posts.getRecommendedPosts", map).url;
        }
        Optional<String> next = Optional.fromNullable(nextUrl);

        String addPostUrl = null;
        if (user != null) {
            addPostUrl = "/posts";
        }
        Optional<String> addPost = Optional.fromNullable(addPostUrl);

        PostsListResource postsListResource = PostsListResource.convertToPostsListResource(postsList, addPost, next);
        renderJSON(convertToHalResponse(postsListResource));
    }

    private static long getPostsCount() {
        return Post.count();
    }

    private static long getPostsByTagCount(String tag) {
        return Post.count("content like ?1", "%#" + tag + "%");
    }

    @Secured
    public static void createPost() {
        User user = Security.getAuthenticatedUser();

        PostRequest postJSON = new GsonBuilder().create().
                fromJson(new InputStreamReader(request.body), PostRequest.class);

        if (!postJSON.isValid()) {
            badRequest();
        }

        Post post = new Post();
        post.content = postJSON.content;
        post.author = user;
        post.save();

        String location = request.host + "/posts/" + post.id;
        response.headers.put("Location", new Header("Location", location));
        response.setContentTypeIfNotSet("application/json");
        response.status = 201;

        HalBaseResource postResponseJSON = PostResource.convertSinglePostResponse(post, user);
        renderJSON(convertToHalResponse(postResponseJSON));
    }

    public static void getPost(long id) {
        Post post = Post.findById(id);
        if (post == null) {
            notFound();
        }
        User user = Security.getAuthenticatedUser();

        HalResource postResponseJSON = PostResource.convertSinglePostResponse(post, user);
        renderJSON(convertToHalResponse(postResponseJSON));
    }

    @Secured
    public static void editPost(long id) {
        PostRequest postJSON = new GsonBuilder().create().
                fromJson(new InputStreamReader(request.body), PostRequest.class);
        Post post = Post.findById(id);
        if (post == null) {
            notFound();
        }

        Security.verifyOwner(post.author);

        post.content = postJSON.content;
        post.save();

        response.setContentTypeIfNotSet("application/json");
        User user = Security.getAuthenticatedUser();

        HalBaseResource postResponseJSON = PostResource.convertSinglePostResponse(post, user);
        renderJSON(convertToHalResponse(postResponseJSON));
    }

    @Secured
    public static void deletePost(long id) {
        Post post = Post.findById(id);
        if (post == null) {
            notFound();
        }

        Security.verifyOwner(post.author);

        post.delete();
        ok();
    }

    @Util
    public static void renderPostsListJson(List<Post> postsList, Optional<String> addPost, Optional<String> next) {
        PostsListResource postsListResource = PostsListResource.convertToPostsListResource(postsList, addPost, next);
        renderJSON(convertToHalResponse(postsListResource));
    }

}
