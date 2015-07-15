package controllers.users;

import models.User;
import controllers.posts.PostResource;

public class UserResponse {

	public long id;
	public String displayName;
	public String photo;
	public String loginName;
	public String photoProvider;
	
	public static UserResponse convertToJson(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.id = user.id;
		userResponse.displayName = user.getDisplayName();
		userResponse.photo = user.getPhoto();
		userResponse.loginName = user.loginName;
		userResponse.photoProvider = user.photoProvider.toString();
		return userResponse;
	}
}
