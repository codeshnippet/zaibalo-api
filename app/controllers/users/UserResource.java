package controllers.users;

import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalLink;
import models.User;
import controllers.posts.PostResource;

import java.util.Date;

public class UserResource extends HalBaseResource {

	public long id;
	public String displayName;
	public String photo;
	public String loginName;
	public String photoProvider;
    public String about;
    public long postsCount;
    public long commentsCount;

    @HalLink(name = "editUser")
    public String editUser;

    private UserResource(){}

	public static UserResource convertToJson(User user, User authUser) {
		UserResource userResponse = new UserResource();
		userResponse.id = user.id;
		userResponse.displayName = user.getDisplayName();
		userResponse.photo = user.getPhoto();
		userResponse.loginName = user.loginName;
		userResponse.photoProvider = user.photoProvider.toString();
        userResponse.about = user.about;
        if(authUser != null && user.id.equals(authUser.id)){
            userResponse.editUser = "/users/" + user.id;
        }
		return userResponse;
	}

    public static UserResource convertToJson(User user) {
        return convertToJson(user, null);
    }

}
