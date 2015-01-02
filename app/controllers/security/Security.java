package controllers.security;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Util;

public class Security extends Controller {

	@Before
	public static void checkAccess() {
		Secured secured = getActionAnnotation(Secured.class);
		if (secured != null) {
			User user = getAuthenticatedUser();
			if(user == null || !user.getPassword().equals(User.hashPassword(request.password))){
				unauthorized();
			}
		}
	}

	@Util
	public static User getAuthenticatedUser() {
		return User.findByLoginName(request.user);
	}
}
