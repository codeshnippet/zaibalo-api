package controllers.users;

import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.posts.Posts;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Users extends Controller {

	public static void createUser() {
		UserRequest userRequest = new GsonBuilder().create().fromJson(new InputStreamReader(request.body), UserRequest.class);

		User user = new User();
		UserRequest.populateUserFromUserRequest(userRequest, user);
		user.save();

		response.setContentTypeIfNotSet("application/json");
		String location = request.host + "/users/" + user.id;
		response.headers.put("Location", new Header("Location", location));
		
		response.status = 201;
        renderJSON(UserResponse.convertToJson(user));
	}
	
	public static void getUserByLogin(String loginName) {
		User user = User.find("byLoginName", loginName).first();
		if (user == null) {
			notFound();
		}
		response.setContentTypeIfNotSet("application/json");
		renderJSON(UserResponse.convertToJson(user));
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
		renderJSON(UserResponse.convertToJson(user));
	}
	
	public static void getUserPosts(String loginName, String sort, int from, int limit){
		User user = User.find("byLoginName", loginName).first();
		if (user == null) {
			notFound();
		}
		Posts.renderPostsListJson(sort, from, limit, "author = ?", user);
	}
	
	public static void getUserPostsCount(String loginName){
		User user = User.find("byLoginName", loginName).first();
		if (user == null) {
			notFound();
		}
		long count = Post.count("byAuthor", user);
		Posts.renderCountJson(count);
	}
}