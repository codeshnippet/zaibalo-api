package services;

import java.util.HashSet;
import java.util.Set;

import models.ServiceProvider;

public class OauthServiceImpl implements OauthService {

    private Set<SocialApiClient> set = new HashSet<SocialApiClient>();

    public OauthServiceImpl(){
        set.add(new GoogleApiClient());
        set.add(new FacebookApiClient());
    }

    @Override
    public boolean validateAccessToken(String accessToken, ServiceProvider serviceProvider) {
        for(SocialApiClient client: set){
            if(client.isProvider(serviceProvider)){
                return client.validateAccessToken(accessToken);
            }
        }
        return false;
    }

}
