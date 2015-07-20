package controllers.authentication;

import models.User;
import controllers.users.UserResource;

public class LoginResource {

	public UserResource user;
	public String token;
	
	public static LoginResource convertToJson(User user){
		UserResource userResponse = UserResource.convertToJson(user);
		LoginResource loginResponse = new LoginResource();
		loginResponse.user = userResponse;
		loginResponse.token = user.token;
		return loginResponse;
	}
}
