package controllers.users;

import java.io.InputStreamReader;

import models.Comment;
import models.Post;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.mvc.Http.Header;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.BasicController;
import controllers.authentication.LoginDTO;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Users extends BasicController {

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

        long postsCount = Post.count("byAuthor", user);
        long commentsCount = Comment.count("byAuthor", user);

        User authUser = Security.getAuthenticatedUser();
        UserResource userResource = UserResource.convertToJson(user, authUser);
        userResource.postsCount = postsCount;
        userResource.commentsCount = commentsCount;

        response.setContentTypeIfNotSet("application/json");
		renderJSON(convertToHalResponse(userResource));
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

        long postsCount = Post.count("byAuthor", user);
        long commentsCount = Comment.count("byAuthor", user);

        User authUser = Security.getAuthenticatedUser();
        UserResource userResource = UserResource.convertToJson(user, authUser);
        userResource.postsCount = postsCount;
        userResource.commentsCount = commentsCount;
        
		response.setContentTypeIfNotSet("application/json");
		renderJSON(convertToHalResponse(userResource));
	}

	private static void failure(String errorMessage) {
		writeToResponseBody(errorMessage);
		badRequest();
	}

}
