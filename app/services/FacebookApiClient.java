package services;

import models.Oauth;
import models.ServiceProvider;

public class FacebookApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.FACEBOOK;

    @Override
    public Oauth getOauthUser(String accessToken) {
        return null;
    }

    @Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }
}
