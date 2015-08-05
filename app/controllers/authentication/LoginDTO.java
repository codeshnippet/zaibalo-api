package controllers.authentication;

import models.User;
import controllers.users.UserResource;

public class LoginDTO {

	public UserResource user;
	public String token;
	
	public static LoginDTO toDTO(User user){
		UserResource userResponse = UserResource.convertToJson(user);
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.user = userResponse;
		loginDTO.token = user.token;
		return loginDTO;
	}
}
