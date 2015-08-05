package services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import models.Oauth;
import models.ServiceProvider;

public class OauthServiceImpl implements OauthService {

	private static OauthServiceImpl service;
	
    private Set<SocialApiClient> set = new HashSet<SocialApiClient>();

    private OauthServiceImpl(){
        set.add(new GoogleApiClient());
        set.add(new FacebookApiClient());
    }

    @Override
    public Oauth getOauthUser(String accessToken, ServiceProvider serviceProvider) throws IOException {
        for(SocialApiClient client: set){
            if(client.isProvider(serviceProvider)){
                return client.getOauthUser(accessToken);
            }
        }
        return null;
    }

    public static OauthServiceImpl getInstance(){
    	if(service == null){
    		service = new OauthServiceImpl();
    	}
    	return service;
    }
}
