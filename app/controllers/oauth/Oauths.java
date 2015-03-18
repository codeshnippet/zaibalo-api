package controllers.oauth;

import java.io.InputStreamReader;

import models.Oauth;
import models.Oauth.OauthProvider;
import models.User;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.GsonBuilder;

import controllers.authentication.LoginRequest;
import controllers.authentication.LoginResponse;
import play.mvc.Controller;

public class Oauths extends Controller {

	public static void login(){
		OauthRequest oauthRequest = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), OauthRequest.class);
		
		if(!oauthRequest.isValid()){
			badRequest();
		}
		
		Oauth oauth = Oauth.findByClienIdAndProvider(oauthRequest.clientId, OauthProvider.valueOf(oauthRequest.provider));
		if(oauth == null){
			User user = new User();
			OauthRequest.populateUserFromOauthRequest(oauthRequest, user);
			user.save();
			
			oauth = OauthRequest.createFromOauthRequest(oauthRequest, user);
			oauth.save();
		}

		renderJSON(LoginResponse.convertToJson(oauth.user));
	}

}
