package services;

import java.io.IOException;

import models.Oauth;
import models.ServiceProvider;
import models.User;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

public class GoogleApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.GOOGLE_PLUS;

    @Override
    public Oauth getOauthUser(String accessToken) throws IOException {
        
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);   
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
                 "Oauth2").build();
        
        Userinfoplus userinfo = null;
		userinfo = oauth2.userinfo().get().execute();

        User user = new User(userinfo.getId(), userinfo.getName());
        user.email = userinfo.getEmail();
        user.setPhoto(userinfo.getPicture());
        user.photoProvider = PROVIDER;

        Oauth oauth = new Oauth();
        oauth.provider = PROVIDER;
        oauth.clientId = userinfo.getId();
        
        oauth.user = user;
        
        return oauth;
    }

    @Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }

}
