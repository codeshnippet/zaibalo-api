package controllers.users;

import models.User;
import controllers.posts.PostResponse;

public class UserResponse {

	public long id;
	public String displayName;
	
	public static UserResponse convertToJson(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.id = user.id;
		userResponse.displayName = user.displayName;
		return userResponse;
	}
}
