package controllers.oauth;

import java.io.IOException;
import java.io.InputStreamReader;

import models.Oauth;
import models.ServiceProvider;
import models.User;
import play.Logger;
import play.mvc.Controller;
import services.OauthService;
import services.OauthServiceImpl;

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
		
		Oauth oauthUser = null;
		try {
			oauthUser = oauthService.getOauthUser(oauthRequest.accessToken, ServiceProvider.valueOf(oauthRequest.provider));
		} catch (IOException e) {
			error(e.getMessage());
		}
		
		boolean exists = Oauth.isExisting(oauthUser.clientId, oauthUser.provider);
		if(exists){
			oauthUser = Oauth.findByClienIdAndProvider(oauthUser.clientId, oauthUser.provider);
		} else {
			oauthUser.user.save();
			oauthUser.save();
		}

		renderJSON(LoginDTO.toDTO(oauthUser.user));
	}

}
