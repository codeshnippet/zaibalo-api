package services;

import models.ServiceProvider;


public interface OauthService {

    public boolean validateAccessToken(String accessToken, ServiceProvider serviceProvider);

}
