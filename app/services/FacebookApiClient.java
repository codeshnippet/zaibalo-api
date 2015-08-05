package services;

import models.ServiceProvider;

public class FacebookApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.FACEBOOK;

    @Override
    public boolean validateAccessToken(String accessToken) {
        return true;
    }

    @Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }
}
