package controllers.posts;

import java.io.InputStreamReader;
import java.util.*;

import models.Post;
import models.PostRating;
import models.User;
import play.mvc.Http.Header;
import play.mvc.Router;
import play.mvc.Util;
import play.mvc.With;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalResource;

import com.google.common.base.Optional;
import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.posts.service.PostsService;
import controllers.posts.service.impl.PostsServiceImpl;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Posts extends BasicController {

    private static final int DEFAULT_FROM = 0;
    private static final int DEFAULT_LIMIT = 0;

    private static PostsService postsService = new PostsServiceImpl();

    public static void getPostsByTag(String tag, int from, int limit) {
        from = (from == 0) ? DEFAULT_FROM : from;
        limit = (limit == 0) ? DEFAULT_LIMIT : limit;

        List<Post> postsList = postsService.getPostsByTag(tag, Post.SortBy.CREATION_DATE, from, limit);

        long count = getPostsByTagCount(tag);

        Map<String, Object> params = getNextPageParams(from, limit, new AbstractMap.SimpleEntry<String, Object>("tag", tag));
        boolean nextPageAvailable = isNextPageAvailable(from, postsList.size(), count);
        Optional<String> next = getNextPageUrl("posts.Posts.getPostsByTag", nextPageAvailable, params);

        Optional<String> addPost = getAddPostUrl(Security.getAuthenticatedUser() != null);

        renderPostsListJson(postsList, addPost, next);
    }

    public static void getPosts(int from, int limit) {
        from = (from == 0) ? DEFAULT_FROM : from;
        limit = (limit == 0) ? DEFAULT_LIMIT : limit;

        List<Post> postsList = postsService.getLatestPosts(from, limit);

        long count = getPostsCount();

        Map<String, Object> params = getNextPageParams(from, limit);
        boolean nextPageAvailable = isNextPageAvailable(from, postsList.size(), count);
        Optional<String> next = getNextPageUrl("posts.Posts.getPosts", nextPageAvailable, params);

        Optional<String> addPost = getAddPostUrl(Security.getAuthenticatedUser() != null);

        renderPostsListJson(postsList, addPost, next);
    }

    public static void getRecommendedPosts(int from, int limit, long threshold) {
        User user = Security.getAuthenticatedUser();

        from = (from == 0) ? DEFAULT_FROM : from;
        limit = (limit == 0) ? DEFAULT_LIMIT : limit;
        threshold = (threshold == 0) ? PostRating.getMaxRecoThreshold(user) : threshold;

        List<Post> postsList = postsService.getRecommendedPosts(user, from, limit, threshold);

        long count = postsService.getRecommendedPostsCount(user, threshold);

        Map<String, Object> params = getNextPageParams(from, limit, new AbstractMap.SimpleEntry<String, Object>("threshold", threshold));
        boolean nextPageAvailable = isNextPageAvailable(from, postsList.size(), count);
        Optional<String> next = getNextPageUrl("posts.Posts.getRecommendedPosts", nextPageAvailable, params);

        Optional<String> addPost = getAddPostUrl(user != null);

        PostsListResource postsListResource = PostsListResource.convertToPostsListResource(postsList, addPost, next);
        renderJSON(convertToHalResponse(postsListResource));
    }

    public static void getUserPosts(String loginName, int from, int limit) {
        from = (from == 0) ? DEFAULT_FROM : from;
        limit = (limit == 0) ? DEFAULT_LIMIT : limit;

        User user = User.find("byLoginName", loginName).first();
        if (user == null) {
            notFound();
        }

        List<Post> postsList = postsService.getUserPosts(user, Post.SortBy.CREATION_DATE, from, limit);

        long count = Post.count("byAuthor", user);

        Map<String, Object> params = getNextPageParams(from, limit, new AbstractMap.SimpleEntry<String, Object>("loginName", loginName));
        boolean nextPageAvailable = isNextPageAvailable(from, postsList.size(), count);
        Optional<String> next = getNextPageUrl("posts.Posts.getUserPosts", nextPageAvailable, params);

        Optional<String> addPost = getAddPostUrl(Security.getAuthenticatedUser() != null);

        Posts.renderPostsListJson(postsList, addPost, next);
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

        HalBaseResource postResponseJSON = PostResource.convertSinglePostResponse(post);
        renderJSON(convertToHalResponse(postResponseJSON));
    }

    public static void getPost(long id) {
        Post post = Post.findById(id);
        if (post == null) {
            notFound();
        }

        renderPostsListJson(Arrays.asList(post), Optional.absent(), Optional.absent());
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

        HalBaseResource postResponseJSON = PostResource.convertSinglePostResponse(post);
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
    private static void renderPostsListJson(List<Post> postsList, Optional addPost, Optional next) {
        PostsListResource postsListResource = PostsListResource.convertToPostsListResource(postsList, addPost, next);
        renderJSON(convertToHalResponse(postsListResource));
    }

    @Util
    private static Optional<String> getNextPageUrl(String controllerName, boolean nextPageAvailable, Map<String, Object> params) {
        String nextUrl = null;
        if (nextPageAvailable) {
            nextUrl = Router.reverse(controllerName, params).url;
        }
        return Optional.fromNullable(nextUrl);
    }

    @Util
    private static boolean isNextPageAvailable(int from, int listSize, long count) {
        return from + listSize < count;
    }

    @Util
    public static Optional<String> getAddPostUrl(boolean isUserAuthenticated) {
        String addPostUrl = null;
        if (isUserAuthenticated) {
            addPostUrl = Router.reverse("posts.Posts.createPost").url;
        }
        return Optional.fromNullable(addPostUrl);
    }

    @Util
    private static Map<String, Object> getNextPageParams(int from, int limit, Map.Entry<String, Object>... additionalParams) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("from", from + limit);
        params.put("limit", limit);
        for (Map.Entry<String, Object> entry : additionalParams) {
            params.put(entry.getKey(), entry.getValue());
        }
        return params;
    }

}
