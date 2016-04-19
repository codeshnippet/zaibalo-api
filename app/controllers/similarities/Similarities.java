package controllers.similarities;

import controllers.security.Secured;
import controllers.security.Security;
import models.PostRating;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Security.class)
public class Similarities extends Controller {

    @Secured
    public static void getMaxRecThreshold(){
        User user = Security.getAuthenticatedUser();
        long count = PostRating.getMaxRecoThreshold(user);
        renderJSON("{\"maxRecThreshold\":" + count +"}");
    }
}
