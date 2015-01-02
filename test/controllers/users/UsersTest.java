package controllers.users;

import models.User;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controllers.BasicFunctionalTest;

public class UsersTest extends BasicFunctionalTest {

	private static final String EHE_HASHED = "c314409d89dea3fb1d2fc4b63e88b7fc";
	private static final String SECRET_HASHED = "5ebe2294ecd0e0f08eab7690d2a6ee69";
	private static final String PASS_HASHED = "1a1dc91c907325c69271ddf0c944bc72";
	public static final String FRANKY_LOGIN_NAME = "franky";
	public static final String BILLY_LOGIN_NAME = "billy";
	private static final long WRONG_ID = 286684l;

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetUser() {
		Fixtures.loadModels("data/user.yml");

		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
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
		assertStatus(201, response);
		assertContentType("application/json", response);

		User user = User.findByLoginName("johny");
		assertNotNull(user);
		assertEquals(PASS_HASHED, user.getPassword());
		assertEquals("Boy", user.displayName);
		assertEquals("johny@gmail.com", user.email);
		
		assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
	}
	
	@Test
	public void testUserCreationWithOtherParameters() {
		POST("/users", "application/json", new Gson().toJson(createUserRequest("bill", "secret", "Cosby", "billc@gmail.com")));

		User user = User.findByLoginName("bill");
		assertNotNull(user.id);
		assertNotNull(user.registrationDate);
		assertEquals(SECRET_HASHED, user.getPassword());
		assertEquals("Cosby", user.displayName);
		assertEquals("billc@gmail.com", user.email);
	}
	
	@Test
	public void testUserEditing(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
		
		Request request = getAuthRequest();
		Response response = PUT(request, "/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertIsOk(response);
		assertContentType("application/json", response);
		
		user.refresh();
		assertEquals("Jackson", user.displayName);
		assertEquals(EHE_HASHED, user.getPassword());
		assertEquals("moondance@gmail.com", user.email);
		assertEquals("Mike", user.loginName);
		
		UserResponse userResponse = new GsonBuilder().create().fromJson(response.out.toString(), UserResponse.class);
		assertEquals(Long.valueOf(user.id), Long.valueOf(userResponse.id));
		assertEquals("Jackson", userResponse.displayName);
	}
	
	@Test
	public void testUserEditingIsSecure(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
		Response response = PUT("/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertStatus(401, response);
	}
	
	@Test
	public void testUserEditingCanBeDoneByUserOnly(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
		Request request = getAuthRequest(BILLY_LOGIN_NAME, "secret");
		Response response = PUT(request, "/users/" + user.id, "application/json", new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertStatus(403, response);
	}
	
	@Test
	public void testUserEditingWithWrongId(){
		Fixtures.loadModels("data/user.yml");
		
		Request request = getAuthRequest();
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
