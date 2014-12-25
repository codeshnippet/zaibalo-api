package controllers.security;

import models.User;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Cookie;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.GsonBuilder;

import controllers.posts.PostRequest;
import controllers.security.LoginResponse;
import controllers.security.Security;
import controllers.users.UsersTest;

public class SecurityTest extends FunctionalTest {
	
	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data/user.yml");
	}
	
	@Test
	public void testLogin(){
		//Check response has authentication token
		Response response = POST("/login", "application/json", "{\"username\":\"franky\", \"password\":\"secret\"}");
		assertIsOk(response);
		LoginResponse authTokenJson = new GsonBuilder().create().fromJson(response.out.toString(), LoginResponse.class);
		assertNotNull(authTokenJson.authToken);
		assertFalse(StringUtils.isEmpty(authTokenJson.authToken));
		
		//Check token matches user token
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertEquals(user.authToken, authTokenJson.authToken);
	}
	
	@Test
	public void testLoginWithWrongPassword() {
		Response response = POST("/login", "application/json", "{\"username\":\"franky\", \"password\":\"WRONG_SECRET\"}");
		assertStatus(403, response);
		assertEquals("<h1>Authentication failed</h1>", response.out.toString());
		
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertEquals("random-auth-token-123", user.authToken);
	}
	
	@Test
	public void testLoginWithWrongUsername() {
		Response response = POST("/login", "application/json", "{\"username\":\"WRONG_USERNAME\", \"password\":\"secret\"}");
		assertStatus(403, response);
		assertEquals("<h1>Authentication failed</h1>", response.out.toString());
		
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertEquals("random-auth-token-123", user.authToken);
	}

}
