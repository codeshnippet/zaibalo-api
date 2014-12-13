package controllers.users;

import java.io.InputStreamReader;

import models.User;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Header;

import com.google.gson.GsonBuilder;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Users extends Controller {

	public static void createUser() {
		UserRequest userRequest = new GsonBuilder().create().fromJson(new InputStreamReader(request.body), UserRequest.class);

		User user = new User();
		populateUserFromUserRequest(userRequest, user);
		user.save();

		response.setContentTypeIfNotSet("application/json");
		String location = request.host + "/users/" + user.id;
		response.headers.put("Location", new Header("Location", location));
		renderJSON(UserResponse.convertToJson(user));
	}

	public static void getUser(long id) {
		User user = User.findById(id);
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
		if(user.id != Security.connected(request).id){
			forbidden();
		}
		
		populateUserFromUserRequest(userRequest, user);
		user.save();

		response.setContentTypeIfNotSet("application/json");
		renderJSON(UserResponse.convertToJson(user));
	}

	private static void populateUserFromUserRequest(UserRequest userRequest, User user) {
		user.loginName = userRequest.loginName;
		user.password = userRequest.password;
		user.displayName = userRequest.displayName;
		user.email = userRequest.email;
	}
}
