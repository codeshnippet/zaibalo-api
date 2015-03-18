package controllers.authentication;

import java.io.InputStreamReader;

import models.User;

import org.apache.commons.codec.digest.DigestUtils;

import play.mvc.Controller;

import com.google.gson.GsonBuilder;
import com.sun.mail.imap.protocol.Status;

import controllers.users.UserResponse;

public class Login extends Controller {

	public static void login(){
		LoginRequest loginRequest = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), LoginRequest.class);
		User user = User.findByLoginName(loginRequest.loginName);
		if(user == null){
			failure();
		}
		
		String passwordMd5 = DigestUtils.md5Hex(loginRequest.password);
		if(!user.getPassword().equals(passwordMd5)){
			failure();
		}
		
		success(user);
	}
	
	private static void success(User user) {
		renderJSON(LoginResponse.convertToJson(user));
	}

	private static void failure() {
		badRequest();
	}
}
