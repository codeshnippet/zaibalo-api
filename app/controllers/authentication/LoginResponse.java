package controllers.authentication;

import models.User;
import controllers.users.UserResponse;

public class LoginResponse {

	public UserResponse user;
	public String token;
	
	public static LoginResponse convertToJson(User user){
		UserResponse userResponse = UserResponse.convertToJson(user);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.user = userResponse;
		loginResponse.token = user.token;
		return loginResponse;
	}
}
