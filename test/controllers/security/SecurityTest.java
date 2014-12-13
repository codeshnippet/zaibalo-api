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
		
		//Check cookie gets created
		Cookie authTokenCookie = response.cookies.get("authToken");
		assertNotNull(authTokenCookie);
		assertFalse(StringUtils.isEmpty(authTokenCookie.value));
		assertEquals(authTokenJson.authToken, authTokenCookie.value);
	}
	
	@Test
	public void testLoginWithWrongPassword() {
		Response response = POST("/login", "application/json", "{\"username\":\"franky\", \"password\":\"WRONG_SECRET\"}");
		assertStatus(401, response);
		assertTrue(StringUtils.isEmpty(response.out.toString()));
		
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertEquals("random-auth-token-123", user.authToken);
	}
	
	@Test
	public void testLoginWithWrongUsername() {
		Response response = POST("/login", "application/json", "{\"username\":\"WRONG_USERNAME\", \"password\":\"secret\"}");
		assertStatus(401, response);
		assertTrue(StringUtils.isEmpty(response.out.toString()));
		
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertEquals("random-auth-token-123", user.authToken);
	}
	
	@Test
	public void testLogout(){
		Request request = newRequest();
    	request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", "random-auth-token-123"));
    	
		Response response = POST(request, "/logout");
		assertIsOk(response);
		Cookie cookie = response.cookies.get(Security.AUTH_TOKEN);
		assertNotNull(cookie);
		assertNotNull(cookie.value);
		assertEquals(Integer.valueOf(0), cookie.maxAge);
		
		User user = User.findByLoginName(UsersTest.FRANKY_LOGIN_NAME);
		assertNotNull(user);
		assertNull(user.authToken);
	}
}
