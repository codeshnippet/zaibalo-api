package controllers.oauth;

import java.io.InputStreamReader;

import models.Oauth;
import models.ServiceProvider;
import models.User;
import play.mvc.Controller;
import services.OauthService;
import services.OauthServiceImpl;

import com.google.gson.GsonBuilder;

import controllers.authentication.LoginResource;

public class Oauths extends Controller {

	public static void login(){
		OauthRequest oauthRequest = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), OauthRequest.class);
		
		if(!oauthRequest.isValid()){
			badRequest();
		}
		
		Oauth oauth = Oauth.findByClienIdAndProvider(oauthRequest.clientId, ServiceProvider.valueOf(oauthRequest.provider));
		if(oauth == null){
			User user = OauthRequest.transformOauthRequestToUser(oauthRequest);
			user.save();
			
			oauth = OauthRequest.createFromOauthRequest(oauthRequest, user);
			oauth.save();
		}

		renderJSON(LoginResource.convertToJson(oauth.user));
	}

}
