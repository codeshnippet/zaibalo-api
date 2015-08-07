package controllers.oauth;

import java.io.IOException;
import java.io.InputStreamReader;

import models.Oauth;
import models.ServiceProvider;
import play.mvc.Controller;
import services.social.OauthService;
import services.social.impl.OauthServiceImpl;
import services.social.model.UserInfo;

import com.google.gson.GsonBuilder;

import controllers.authentication.LoginDTO;

public class Oauths extends Controller {

	public static void login(){
		OauthRequest oauthRequest = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), OauthRequest.class);
		
		if(!oauthRequest.isValid()){
			badRequest();
		}
		
		OauthService oauthService = OauthServiceImpl.getInstance();
		ServiceProvider serviceProvider = ServiceProvider.valueOf(oauthRequest.provider);
		UserInfo userInfo = oauthService.getUserInfo(oauthRequest.accessToken, serviceProvider);
		
		boolean exists = Oauth.isExisting(userInfo.id, serviceProvider);
		
		Oauth oauth = null;
		if(exists){
			oauth = Oauth.findByClienIdAndProvider(userInfo.id, serviceProvider);
		} else {
			oauth = oauthService.createOauthFromUserInfo(userInfo, serviceProvider);
		}

		renderJSON(LoginDTO.toDTO(oauth.user));
	}

}
