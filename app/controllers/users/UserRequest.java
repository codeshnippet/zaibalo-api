package controllers.users;

import org.apache.commons.lang.StringUtils;

import models.User;

public class UserRequest {
	public String loginName;
	public String password;
	public String email;
	public String displayName;
	public String photo;
	
	public static void populateUserFromUserRequest(UserRequest userRequest, User user) {
		user.loginName = userRequest.loginName;
		user.setPassword(userRequest.password);
		user.displayName = userRequest.displayName;
		user.email = userRequest.email;
		user.photo = userRequest.photo;
	}

	public static void updateUserFromUserRequest(UserRequest userRequest, User user) {
		if(StringUtils.isNotBlank(userRequest.loginName)){
			user.loginName = userRequest.loginName;
		}
		if(StringUtils.isNotBlank(userRequest.password)){
			user.setPassword(userRequest.password);
		}
		if(StringUtils.isNotBlank(userRequest.displayName)){
			user.displayName = userRequest.displayName;
		}
		if(StringUtils.isNotBlank(userRequest.email)){
			user.email = userRequest.email;
		}
		if(StringUtils.isNotBlank(userRequest.photo)){
			user.photo = userRequest.photo;
		}
	}
}
