package services;

import java.io.IOException;

import models.Oauth;
import models.ServiceProvider;


public interface OauthService {

    public Oauth getOauthUser(String accessToken, ServiceProvider serviceProvider) throws IOException;

}
