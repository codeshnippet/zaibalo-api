package controllers.oauth;

import com.google.gson.GsonBuilder;
import controllers.authentication.LoginDTO;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;

import java.io.InputStreamReader;

public class Oauths extends Controller {

    public static void login() {
        OauthRequest oauthRequest = new GsonBuilder().create().
                fromJson(new InputStreamReader(request.body), OauthRequest.class);

        if (!oauthRequest.isValid()) {
            badRequest();
        }

        User user = User.findByLoginName(oauthRequest.externalId);

        if (user == null) {
            user = OauthRequest.transformOauthRequestToUser(oauthRequest);
            user.save();
            response.status = 201;
        }

        String location = request.host + "/users/" + user.id;
        response.headers.put("Location", new Http.Header("Location", location));

        response.setContentTypeIfNotSet("application/json");
        renderJSON(LoginDTO.toDTO(user));
    }

}
