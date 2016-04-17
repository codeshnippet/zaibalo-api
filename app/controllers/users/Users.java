package controllers.users;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import controllers.posts.service.PostsService;
import controllers.posts.service.impl.PostsServiceImpl;
import models.Post;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.mvc.Http.Header;
import play.mvc.Router;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.authentication.LoginDTO;
import controllers.posts.Posts;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Users extends BasicController {

    private static PostsService postsService = new PostsServiceImpl();

	public static void createUser() {
		UserRequest userRequest = new GsonBuilder().create().fromJson(new InputStreamReader(request.body), UserRequest.class);

		if (StringUtils.isEmpty(userRequest.loginName)) {
			failure("registration.fail.username.required");
		}

		if (StringUtils.isEmpty(userRequest.password)) {
			failure("registration.fail.password.required");
		}

		if (User.findByLoginName(userRequest.loginName) != null) {
			failure("registration.fail.username.not.free");
		}

		User user = UserRequest.transfromUserRequestToUser(userRequest);
		user.save();

		response.setContentTypeIfNotSet("application/json");
		String location = request.host + "/users/" + user.id;
		response.headers.put("Location", new Header("Location", location));

		response.status = 201;
		renderJSON(LoginDTO.toDTO(user));
	}

	public static void getUserByLogin(String loginName) {
		User user = User.find("byLoginName", loginName).first();
		if (user == null) {
			notFound();
		}
		response.setContentTypeIfNotSet("application/json");
		renderJSON(UserResource.convertToJson(user));
	}

	@Secured
	public static void editUser(long id) {
		UserRequest userRequest = new GsonBuilder().create().fromJson(new InputStreamReader(request.body), UserRequest.class);

		User user = User.findById(id);
		if (user == null) {
			notFound();
		}

		Security.verifyOwner(user);

		UserRequest.updateUserFromUserRequest(userRequest, user);
		user.save();

		response.setContentTypeIfNotSet("application/json");
		renderJSON(UserResource.convertToJson(user));
	}

	public static void getUserPosts(String loginName, int from, int limit) {
        from = (from == 0) ? 0 : from;
        limit = (limit == 0) ? 10 : limit;

        User user = User.find("byLoginName", loginName).first();
        if (user == null) {
            notFound();
        }

        List<Post> postsList = postsService.getUserPosts(user, Post.SortBy.CREATION_DATE, from, limit);

        long count = Post.count("byAuthor", user);

        String nextUrl = null;
        if (postsList.size() < count) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("from", from + 10);
            map.put("loginName", loginName);
            nextUrl = Router.reverse("users.Users.getUserPosts", map).url;
        }
        Optional<String> next = Optional.fromNullable(nextUrl);

        String addPostUrl = null;
        if (Security.getAuthenticatedUser() != null) {
            addPostUrl = Router.reverse("posts.Posts.createPost").url;
        }
        Optional<String> addPost = Optional.fromNullable(addPostUrl);


		Posts.renderPostsListJson(postsList, addPost, next);
	}

	private static void failure(String errorMessage) {
		writeToResponseBody(errorMessage);
		badRequest();
	}

}
