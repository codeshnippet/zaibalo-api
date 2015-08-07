package services.social.impl;

import play.Logger;
import play.Play;
import models.ServiceProvider;
import services.social.SocialApiClient;
import services.social.model.UserInfo;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.User;
import facebook4j.auth.AccessToken;

public class FacebookApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.FACEBOOK;

    @Override
    public UserInfo getUserInfo(String accessToken) {
    	Facebook facebook = new FacebookFactory().getInstance();
    	facebook.setOAuthAppId("", "");
    	facebook.setOAuthPermissions("public_profile,email");
    	facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
    	
    	User me = null;
    	String pictureUrl = null;
    	try {
			me = facebook.getMe();
			pictureUrl = facebook.getPictureURL().toString();
		} catch (FacebookException e) {
			Logger.error("Facebook API error", e);
			throw new RuntimeException(e.getMessage());
		}
        return transformToUserInfo(me, pictureUrl);
    }

    private UserInfo transformToUserInfo(User me, String pictureUrl) {
		UserInfo userInfo = new UserInfo();
		userInfo.id = me.getId();
		userInfo.name = me.getName();
		userInfo.email = me.getEmail();
		userInfo.picture = pictureUrl;
		return userInfo;
	}

	@Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }
}
