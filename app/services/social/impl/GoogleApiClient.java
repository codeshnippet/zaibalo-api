package services.social.impl;

import java.io.IOException;

import play.Logger;
import models.ServiceProvider;
import services.social.SocialApiClient;
import services.social.model.UserInfo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

public class GoogleApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.GOOGLE_PLUS;

    @Override
    public UserInfo getUserInfo(String accessToken) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);   
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
                 "Oauth2").build();
        
        Userinfoplus userInfo;
		try {
			userInfo = oauth2.userinfo().get().execute();
		} catch (IOException e) {
			Logger.error("Network error", e);
			throw new RuntimeException(e.getMessage());
		}
        return transformToUserInfo(userInfo);
    }

    private UserInfo transformToUserInfo(Userinfoplus userInfoplus) {
		UserInfo userInfo = new UserInfo();
		userInfo.id = userInfoplus.getId();
		userInfo.email = userInfoplus.getEmail();
		userInfo.name = userInfoplus.getName();
		userInfo.picture = userInfoplus.getPicture();
		return userInfo;
	}

	@Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }

}
