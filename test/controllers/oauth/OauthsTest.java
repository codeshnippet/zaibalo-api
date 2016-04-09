package controllers.oauth;

import com.google.gson.Gson;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class OauthsTest extends FunctionalTest {

    private static final String APPLICATION_JSON = "application/json";

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testRegisterOauthUser() {
        Http.Response response = POST("/oauth-login", APPLICATION_JSON, new Gson().toJson(createOauthRequest("ext123", "Johny Brown", "http://img.com/a.jpg", "johny@gmail.com")));
        assertStatus(201, response);
        assertContentType(APPLICATION_JSON, response);

        User user = User.findByLoginName("ext123");
        assertNotNull(user);
        assertEquals("ext123", user.loginName);
        assertEquals("Johny Brown", user.getDisplayName());
        assertEquals("johny@gmail.com", user.email);
        assertEquals("http://img.com/a.jpg", user.getPhoto());
        assertNotNull(user.token);
        assertNotNull(user.getPassword());

        assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
    }

    @Test
    public void testLoginOauthUser(){
        Fixtures.loadModels("data/user.yml");

        Http.Response response = POST("/oauth-login", APPLICATION_JSON, new Gson().toJson(createOauthRequest("ext123", "Other", "http://img.com/ab.jpg", "johny222@gmail.com")));
        assertStatus(200, response);
        assertContentType(APPLICATION_JSON, response);

        User user = User.findByLoginName("ext123");
        assertNotNull(user);
        assertEquals("ext123", user.loginName);
        assertEquals("Johny Brown", user.getDisplayName());
        assertEquals("johny@gmail.com", user.email);
        assertEquals("http://img.com/a.jpg", user.getPhoto());

        assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
    }

    private OauthRequest createOauthRequest(String externalId, String displayName, String photo, String email) {
        OauthRequest request = new OauthRequest();
        request.externalId = externalId;
        request.displayName = displayName;
        request.photo = photo;
        request.email = email;
        return request;
    }
}
