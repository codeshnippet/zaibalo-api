package services;

import models.ServiceProvider;

public interface SocialApiClient {

    public boolean validateAccessToken(String accessToken);
    public boolean isProvider(ServiceProvider provider);
}
