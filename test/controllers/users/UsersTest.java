package controllers.users;

import models.User;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UsersTest extends FunctionalTest {

	private static final String FRANKY_AUTH_TOKEN = "random-auth-token-123";
	private static final String FRANKY_LOGIN_NAME = "franky";
	private static final String BILLY_AUTH_TOKEN = "billy-auth-token-123";
	private static final long WRONG_ID = 286684l;

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetUser() {
		Fixtures.loadModels("data/user.yml");

		User user = User.find("byLoginName", FRANKY_LOGIN_NAME).first();
		Response response = GET("/users/" + user.id);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertEquals("{\"id\":" + user.id + ",\"displayName\":\"Superman\"}", response.out.toString());
	}

	@Test
	public void testUserNotFound() {
		Response response = GET("/users/" + WRONG_ID);
		assertIsNotFound(response);
	}

	@Test
	public void testUserCreation() {
		Response response = POST("/users", "application/json", new Gson().toJson(createUserRequest("johny", "pass", "Boy", "johny@gmail.com")));
		assertIsOk(response);
		assertContentType("application/json", response);

		User user = User.find("byLoginName", "johny").first();
		assertNotNull(user);
		assertEquals("pass", user.password);
		assertEquals("Boy", user.displayName);
		assertEquals("johny@gmail.com", user.email);
		
		assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
		
		UserResponse userResponse = new GsonBuilder().create().fromJson(response.out.toString(), UserResponse.class);
		assertEquals(Long.valueOf(user.id), Long.valueOf(userResponse.id));
		assertEquals("Boy", userResponse.displayName);
	}
	
	@Test
	public void testUserCreationWithOtherParameters() {
		POST("/users", "application/json", new Gson().toJson(createUserRequest("bill", "secret", "Cosby", "billc@gmail.com")));

		User user = User.find("byLoginName", "bill").first();
		assertNotNull(user.id);
		assertNotNull(user.registrationDate);
		assertEquals("secret", user.password);
		assertEquals("Cosby", user.displayName);
		assertEquals("billc@gmail.com", user.email);
	}
	
	@Test
	public void testUserEditing(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.find("byLoginName", FRANKY_LOGIN_NAME).first();
		
		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", FRANKY_AUTH_TOKEN));
		Response response = PUT(request, "/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertIsOk(response);
		assertContentType("application/json", response);
		
		user.refresh();
		assertEquals("Jackson", user.displayName);
		assertEquals("ehe", user.password);
		assertEquals("moondance@gmail.com", user.email);
		assertEquals("Mike", user.loginName);
		
		UserResponse userResponse = new GsonBuilder().create().fromJson(response.out.toString(), UserResponse.class);
		assertEquals(Long.valueOf(user.id), Long.valueOf(userResponse.id));
		assertEquals("Jackson", userResponse.displayName);
	}
	
	@Test
	public void testUserEditingIsSecure(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.find("byLoginName", FRANKY_LOGIN_NAME).first();
		Response response = PUT("/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertStatus(401, response);
	}
	
	@Test
	public void testUserEditingCanBeDoneByUserOnly(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.find("byLoginName", FRANKY_LOGIN_NAME).first();
		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", BILLY_AUTH_TOKEN));
		Response response = PUT(request, "/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertStatus(403, response);
	}
	
	@Test
	public void testUserEditingWithWrongId(){
		Fixtures.loadModels("data/user.yml");
		
		Request request = newRequest();
		request.headers.put("X-AUTH-TOKEN", new Header("X-AUTH-TOKEN", FRANKY_AUTH_TOKEN));
		Response response = PUT(request, "/users/" + WRONG_ID, "application/json", new Gson().toJson(createUserRequest("Mike", "p", "d", "e@e.e")));
		assertIsNotFound(response);
	}

	private UserRequest createUserRequest(String login, String pass, String displayName, String email) {
		UserRequest userRequest = new UserRequest();
		userRequest.loginName = login;
		userRequest.password = pass;
		userRequest.displayName = displayName;
		userRequest.email = email;
		return userRequest;
	}
}
