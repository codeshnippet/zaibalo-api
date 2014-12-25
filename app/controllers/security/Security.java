package controllers.security;

import java.io.InputStreamReader;

import models.User;

import org.apache.commons.lang.StringUtils;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import controllers.users.Users;

public class Security extends Controller {

	private static final String ANONYMOUS = "Anonymous";
	public final static String AUTH_TOKEN_HEADER = "x-auth-token";

	@Before
	public static void checkAccess() {
		Secured secured = getActionAnnotation(Secured.class);
		if (secured != null) {
			User user = getAuthenticatedUser(request);
			if (user != null) {
				return;
			}
			forbidden("Authentication required");
		}
	}

	public static void login() {
		LoginRequest authentication = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), LoginRequest.class);

		User user = User.findByLoginName(authentication.username);
		if (user != null) {
			if (user.getPassword().equals(User.hashPassword(authentication.password))) {
				LoginResponse authTokenJson = new LoginResponse();
				authTokenJson.authToken = user.createToken();
				authTokenJson.displayName = user.displayName;
				renderJSON(authTokenJson);
			}
		}
		forbidden("Authentication failed");
	}
	
	@Util
	public static User getAuthenticatedUser(Request request) {
		Header authTokenHeader = request.headers.get(Security.AUTH_TOKEN_HEADER);
		if (authTokenHeader != null && authTokenHeader.values.size() == 1 && !StringUtils.isBlank(authTokenHeader.values.get(0))) {
			return User.find("byAuthToken", authTokenHeader.values.get(0)).first();
		}
		return null;
	}
	
	@Util
	public static User getAuthenticatedOrAnonymousUser(Request request) {
		User authenticatedUser = getAuthenticatedUser(request);
		return authenticatedUser == null? getAnonymousUser() : authenticatedUser;
	}

	@Util
	private static User getAnonymousUser() {
		User anonymous = User.findByLoginName(ANONYMOUS);
		return anonymous == null ? createAnonymousUser() : anonymous;
	}

	@Util
	private static User createAnonymousUser() {
		User anonymous = new User();
		anonymous.displayName = ANONYMOUS;
		anonymous.loginName = ANONYMOUS;
		anonymous.save();
		return anonymous;
	}
}
