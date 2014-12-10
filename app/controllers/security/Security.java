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

public class Security extends Controller {

	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public static final String AUTH_TOKEN = "authToken";

	@Before
	public static void checkAccess() {
		Secured secured = getActionAnnotation(Secured.class);
		if (secured != null) {
			User user = connected(request);
			if (user != null) {
				return;
			}
			unauthorized();
		}
	}

	public static void login() {
		LoginRequest authentication = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), LoginRequest.class);

		User user = User.find("byLoginName", authentication.username).first();
		if (user != null) {
			if (user.password.equals(authentication.password)) {
				String authToken = user.createToken();
				response.setCookie(AUTH_TOKEN, authToken);
				
				LoginResponse authTokenJson = new LoginResponse();
				authTokenJson.authToken = authToken;
				renderJSON(authTokenJson);
			}
		}
		unauthorized();
	}
	
	public static void logout(){
		User user = connected(request);
		user.deleteToken();
		response.removeCookie(AUTH_TOKEN);
	}
	
	@Util
	public static User connected(Request request) {
		Header authTokenHeader = request.headers.get(Security.AUTH_TOKEN_HEADER);
		if (authTokenHeader != null && authTokenHeader.values.size() == 1 && !StringUtils.isBlank(authTokenHeader.values.get(0))) {
			return User.find("byAuthToken", authTokenHeader.values.get(0)).first();
		}
		return null;
	}
}
