package play.mvc.results;

import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;

/**
 * A Play Framework hack to get rid of Browser Basic Authentication popup.
 */
public class CustomUnauthorized extends Result {
    
    String realm;
    
    public CustomUnauthorized(String realm) {
        super(realm);
        this.realm = realm;
    }

    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.UNAUTHORIZED;
        response.setHeader("WWW-Authenticate", "Custom realm=\"" + realm + "\"");
    }
}
