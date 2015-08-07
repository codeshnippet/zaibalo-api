package services.social.impl;

import services.social.SocialApiClient;
import services.social.model.UserInfo;
import models.ServiceProvider;

public class FacebookApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.FACEBOOK;

    @Override
    public UserInfo getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }
}
