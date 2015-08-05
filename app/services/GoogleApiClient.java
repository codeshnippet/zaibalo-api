package services;

import java.io.IOException;
import java.security.GeneralSecurityException;

import models.ServiceProvider;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleApiClient implements SocialApiClient {

    private static final ServiceProvider PROVIDER = ServiceProvider.GOOGLE_PLUS;
    
    private JsonFactory mJFactory = new GsonFactory();
    private NetHttpTransport transport = new NetHttpTransport();
    private GoogleIdTokenVerifier mVerifier = new GoogleIdTokenVerifier(transport, mJFactory);

    @Override
    public boolean validateAccessToken(String accessToken) {
        boolean success = false;
        try {
            GoogleIdToken token = GoogleIdToken.parse(mJFactory, accessToken);
            success = mVerifier.verify(token);
        } catch (GeneralSecurityException e) {
            // "Security issue: " + e.getLocalizedMessage();
        } catch (IOException e) {
            // "Network problem: " + e.getLocalizedMessage();
        }

        return success;
    }

    @Override
    public boolean isProvider(ServiceProvider provider){
        return provider.equals(PROVIDER);
    }

}
