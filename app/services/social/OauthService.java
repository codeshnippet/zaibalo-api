package services.social;

import java.io.IOException;

import services.social.model.UserInfo;
import models.Oauth;
import models.ServiceProvider;


public interface OauthService {

    public UserInfo getUserInfo(String accessToken, ServiceProvider serviceProvider);

	public Oauth createOauthFromUserInfo(UserInfo userInfo, ServiceProvider serviceProvider);

}
