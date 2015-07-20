package controllers.authentication;

import java.io.InputStreamReader;

import models.User;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.GsonBuilder;

import controllers.BasicController;

public class Login extends BasicController {

	public static void login(){
		LoginRequest loginRequest = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), LoginRequest.class);
		
		if(StringUtils.isEmpty(loginRequest.loginName)){
			failure("login.fail.username.required");
		}
		
		if(StringUtils.isEmpty(loginRequest.password)){
			failure("login.fail.password.required");
		}
		
		User user = User.findByLoginName(loginRequest.loginName);
		if(user == null){
			failure("login.fail");
		}
		
		String passwordMd5 = DigestUtils.md5Hex(loginRequest.password);
		if(!user.getPassword().equals(passwordMd5)){
			failure("login.fail");
		}
		
		success(user);
	}
	
	private static void success(User user) {
		renderJSON(LoginResource.convertToJson(user));
	}

	private static void failure(String errorMessage) {
		writeToResponseBody(errorMessage);
		badRequest();
	}

}
