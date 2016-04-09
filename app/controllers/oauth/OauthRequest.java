package controllers.oauth;

import models.ServiceProvider;

import models.User;
import org.apache.commons.lang.StringUtils;

public class OauthRequest {

    public String email;
    public String displayName;
    public String photo;
    public String externalId;

	public boolean isValid() {
		return StringUtils.isNotBlank(this.externalId) && StringUtils.isNotBlank(this.displayName)
                && StringUtils.isNotBlank(this.email) && StringUtils.isNotBlank(this.photo);
	}

    public static User transformOauthRequestToUser(OauthRequest oauthRequest) {
        User user = new User(oauthRequest.externalId, oauthRequest.displayName);
        user.setPhoto(oauthRequest.photo);
        user.email = oauthRequest.email;
        return user;
    }
}
