package controllers.users;

import models.User;

public class UserRequest {
	public String loginName;
	public String password;
	public String email;
	public String displayName;
	
	public static void populateUserFromUserRequest(UserRequest userRequest, User user) {
		user.loginName = userRequest.loginName;
		user.setPassword(userRequest.password);
		user.displayName = userRequest.displayName;
		user.email = userRequest.email;
	}
}
