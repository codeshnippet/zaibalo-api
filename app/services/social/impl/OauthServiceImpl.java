package services.social.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import services.social.OauthService;
import services.social.SocialApiClient;
import services.social.model.UserInfo;
import models.Oauth;
import models.ServiceProvider;
import models.User;

public class OauthServiceImpl implements OauthService {

	private static OauthServiceImpl service;

	private Set<SocialApiClient> set = new HashSet<SocialApiClient>();

	private OauthServiceImpl() {
		set.add(new GoogleApiClient());
		set.add(new FacebookApiClient());
	}

	@Override
	public UserInfo getUserInfo(String accessToken, ServiceProvider serviceProvider){
		for (SocialApiClient client : set) {
			if (client.isProvider(serviceProvider)) {
				return client.getUserInfo(accessToken);
			}
		}
		return null;
	}

	public static OauthServiceImpl getInstance() {
		if (service == null) {
			service = new OauthServiceImpl();
		}
		return service;
	}

	@Override
	public Oauth createOauthFromUserInfo(UserInfo userInfo, ServiceProvider serviceProvider) {
		User user = new User(userInfo.id, userInfo.name);
		user.email = userInfo.email;
		user.setPhoto(userInfo.picture);
		user.photoProvider = serviceProvider;
		user.save();
		
		Oauth oauth = new Oauth();
		oauth.provider = serviceProvider;
		oauth.clientId = userInfo.id;
		oauth.user = user;
		
		oauth.save();
		
		return oauth;
	}
}
