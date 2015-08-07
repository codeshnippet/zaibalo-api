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
//    	String appId = Play.configuration.getProperty("facebook.app.id");
//    	String appSecret = Play.configuration.getProperty("facebook.app.secret");
    	
    	Facebook facebook = new FacebookFactory().getInstance();
//    	facebook.setOAuthAppId(appId, appSecret);
//    	facebook.setOAuthPermissions("public_profile,email");
    	facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
    	
    	User me = null;
    	try {
			me = facebook.getMe();
		} catch (FacebookException e) {
			Logger.error("Facebook API error", e);
			throw new RuntimeException(e.getMessage());
		}
    	transformToUserInfo(me);
        return null;
    }

    private UserInfo transformToUserInfo(User me) {
		UserInfo userInfo = new UserInfo();
		userInfo.id = me.getId();
		userInfo.name = me.getName();
		userInfo.email = me.getEmail();
		userInfo.picture = me.getPicture().getURL().toString();
		return userInfo;
	}

	@Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }
}
