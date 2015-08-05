package services;

import java.io.IOException;

import models.Oauth;
import models.ServiceProvider;

public interface SocialApiClient {

    public Oauth getOauthUser(String accessToken) throws IOException;
    public boolean isProvider(ServiceProvider provider);
}
