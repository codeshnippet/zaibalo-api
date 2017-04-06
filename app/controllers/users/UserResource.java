package controllers.users;

import models.User;
import controllers.posts.PostResource;

import java.util.Date;

public class UserResource {

	public long id;
	public String displayName;
	public String photo;
	public String loginName;
	public String photoProvider;
    public String about;
    public long postsCount;
    public long commentsCount;
	
	public static UserResource convertToJson(User user) {
		UserResource userResponse = new UserResource();
		userResponse.id = user.id;
		userResponse.displayName = user.getDisplayName();
		userResponse.photo = user.getPhoto();
		userResponse.loginName = user.loginName;
		userResponse.photoProvider = user.photoProvider.toString();
        userResponse.about = user.about;
		return userResponse;
	}
}
