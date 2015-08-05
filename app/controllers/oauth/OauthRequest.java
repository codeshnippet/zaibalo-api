package controllers.oauth;

import models.Oauth;
import models.ServiceProvider;
import models.User;

import org.apache.commons.lang.StringUtils;

public class OauthRequest {

	public String accessToken;
	public String provider;

	public boolean isValid() {
		return StringUtils.isNotBlank(this.accessToken) && ServiceProvider.contains(this.provider);
	}

	public OauthRequest(String accessToken, ServiceProvider provider) {
		this.accessToken = accessToken;
		this.provider = provider.toString();
	}

/*	public static User transformOauthRequestToUser(OauthRequest oauthRequest) {
		User user = new User(oauthRequest.clientId, oauthRequest.displayName);
		user.email = oauthRequest.email;
		user.setPhoto(oauthRequest.photo);
		user.photoProvider = ServiceProvider.valueOf(oauthRequest.provider);
		return user;
	}
	
	public static Oauth createFromOauthRequest(OauthRequest oauthRequest, User user) {
		Oauth oauth = new Oauth();
		oauth.clientId = oauthRequest.clientId;
		oauth.user = user;
		oauth.provider = ServiceProvider.valueOf(oauthRequest.provider);
		return oauth;
	}*/
}
