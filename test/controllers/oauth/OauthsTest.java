package controllers.oauth;

import models.Oauth;
import models.ServiceProvider;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.Gson;

public class OauthsTest extends FunctionalTest {

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data/oauth.yml");
	}

	@Test
	public void testOauthLoginSuccess() {
		OauthRequest oauthRequest = new OauthRequest("frankOauthClientId123", ServiceProvider.GOOGLE_PLUS, "Franky Sinatra", "a@a.com",
				"p.com/a.jpg");
		Response response = POST("/oauth-login", "application/json", toJson(oauthRequest));
		assertStatus(200, response);
		assertContentType("application/json", response);
		assertContentMatch("\\{.*Superman.*secret_token_123.*\\}", response);
	}

	@Test
	public void testOauthLoginNotExistingClientId() {
		assertEquals(1, Oauth.count());
		OauthRequest oauthRequest = new OauthRequest("frankOauthClientId321", ServiceProvider.GOOGLE_PLUS, "Bob Tiger", "a1@a.com",
				"p.com/a.jpg");
		Response response = POST("/oauth-login", "application/json", toJson(oauthRequest));
		assertStatus(200, response);
		assertContentType("application/json", response);
		assertContentMatch("\\{.*Bob Tiger.*\\}", response);
		assertEquals(2, Oauth.count());
	}

	private String toJson(Object object) {
		return new Gson().toJson(object);
	}
}
