package controllers.users;

import org.apache.commons.lang.StringUtils;

import models.User;

public class UserRequest {
	public String loginName;
	public String password;
	public String email;
	public String displayName;
	public String photo;

	public static User transfromUserRequestToUser(UserRequest userRequest) {
		User user = new User(userRequest.loginName, userRequest.displayName);
		user.setPassword(userRequest.password);
		user.email = userRequest.email;
		user.setPhoto(userRequest.photo);
		return user;
	}

	public static void updateUserFromUserRequest(UserRequest userRequest, User user) {
		if (StringUtils.isNotBlank(userRequest.loginName)) {
			user.loginName = userRequest.loginName;
		}
		if (StringUtils.isNotBlank(userRequest.password)) {
			user.setPassword(userRequest.password);
		}
		if (StringUtils.isNotBlank(userRequest.displayName)) {
			user.setDisplayName(userRequest.displayName);
		}
		if (StringUtils.isNotBlank(userRequest.email)) {
			user.email = userRequest.email;
		}
		if (StringUtils.isNotBlank(userRequest.photo)) {
			user.setPhoto(userRequest.photo);
		}
	}
}
