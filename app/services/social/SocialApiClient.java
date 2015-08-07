package services.social;

import java.io.IOException;

import services.social.model.UserInfo;
import models.Oauth;
import models.ServiceProvider;

public interface SocialApiClient {

    public UserInfo getUserInfo(String accessToken);
    
    public boolean isProvider(ServiceProvider provider);
}
