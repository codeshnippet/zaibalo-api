package controllers.users;

import java.util.List;

import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import controllers.BasicFunctionalTest;

public class UsersTest extends BasicFunctionalTest {

	private static final String APPLICATION_JSON = "application/json";
	private static final String EHE_HASHED = "c314409d89dea3fb1d2fc4b63e88b7fc";
	private static final String SECRET_HASHED = "5ebe2294ecd0e0f08eab7690d2a6ee69";
	private static final String PASS_HASHED = "1a1dc91c907325c69271ddf0c944bc72";
	public static final String FRANKY_LOGIN_NAME = "franky";
	public static final String BILLY_LOGIN_NAME = "billy";
	private static final String WRONG_DISPLAY_NAME = "NOT_EXISTING_USER";

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testGetUser() {
		Fixtures.loadModels("data/user.yml");

		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
		Response response = GET("/users/franky");
		assertIsOk(response);
		assertContentType(APPLICATION_JSON, response);
		UserResponse userResponse = new Gson().fromJson(response.out.toString(), UserResponse.class);
		assertEquals(Long.valueOf(user.id), Long.valueOf(userResponse.id));
		assertEquals("Superman", userResponse.displayName);
		assertEquals("franky", userResponse.loginName);
	}

	@Test
	public void testUserNotFound() {
		Response response = GET("/users/" + WRONG_DISPLAY_NAME);
		assertIsNotFound(response);
	}

	@Test
	public void testUserCreation() {
		Response response = POST("/users", APPLICATION_JSON, new Gson().toJson(createUserRequest("johny", "pass", "Boy", "johny@gmail.com")));
		assertStatus(201, response);
		assertContentType(APPLICATION_JSON, response);

		User user = User.findByLoginName("johny");
		assertNotNull(user);
		assertEquals(PASS_HASHED, user.getPassword());
		assertEquals("Boy", user.displayName);
		assertEquals("johny@gmail.com", user.email);
		
		assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
	}

	@Test
	public void testUserCreationWithMinimalFields() {
		Response response = POST("/users", APPLICATION_JSON, new Gson().toJson(createUserRequest("johny", "pass")));
		assertStatus(201, response);
		assertContentType(APPLICATION_JSON, response);

		User user = User.findByLoginName("johny");
		assertNotNull(user);
		assertEquals(PASS_HASHED, user.getPassword());
		assertEquals("johny", user.displayName);
		assertEquals(null, user.email);
		
		assertHeaderEquals("Location", newRequest().host + "/users/" + user.id, response);
	}
	
	@Test
	public void testUserCreationWithOtherParameters() {
		POST("/users", APPLICATION_JSON, new Gson().toJson(createUserRequest("bill", "secret", "Cosby", "billc@gmail.com")));

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
		String bodyJson = new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com"));
		String url = "/users/" + user.id;
		
		Request request = getAuthRequest(url, APPLICATION_JSON, bodyJson, "PUT", FRANKY_LOGIN_NAME, "secret");
		Response response = PUT(request, url, APPLICATION_JSON, bodyJson);
		assertIsOk(response);
		assertContentType(APPLICATION_JSON, response);
		
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
		Response response = PUT("/users/" + user.id, APPLICATION_JSON, new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com")));
		assertStatus(401, response);
	}
	
	@Test
	public void testUserEditingCanBeDoneByUserOnly(){
		Fixtures.loadModels("data/user.yml");
		
		User user = User.findByLoginName(FRANKY_LOGIN_NAME);
		String bodyJson = new Gson().toJson(createUserRequest("Mike", "ehe", "Jackson", "moondance@gmail.com"));
		String url = "/users/" + user.id;
		
		Request request = getAuthRequest(url, APPLICATION_JSON, bodyJson, "PUT", BILLY_LOGIN_NAME, "secret");
		Response response = PUT(request, url, APPLICATION_JSON, bodyJson);
		
		assertStatus(403, response);
	}
	
	@Test
	public void testUserEditingWithWrongId(){
		Fixtures.loadModels("data/user.yml");
		
		String bodyJson = new Gson().toJson(createUserRequest("Mike", "p", "d", "e@e.e"));
		String url = "/users/" + WRONG_DISPLAY_NAME;
		
		Request request = getAuthRequest(url, APPLICATION_JSON, bodyJson, "PUT", BILLY_LOGIN_NAME, "secret");
		Response response = PUT(request, "/users/" + WRONG_DISPLAY_NAME, APPLICATION_JSON, bodyJson);
		assertIsNotFound(response);
	}
	
	@Test
	public void testGetUserPosts() {
		Fixtures.loadModels("data/user-posts.yml");
		
		User franky = User.findByLoginName("franky");
		
		Response response = GET("/users/" + franky.loginName + "/posts");

		List<Post> postsList = new Gson().fromJson(response.out.toString(), new TypeToken<List<Post>>() {
		}.getType());
		assertEquals(2, postsList.size());
		assertEquals("test content 1", postsList.get(0).content);
		assertEquals("test content 3", postsList.get(1).content);
	}

	@Test
	public void testGetUserPostsCount() {
		Fixtures.loadModels("data/user-posts.yml");
		
		User franky = User.findByLoginName("franky");
		
		Response response = GET("/users/" + franky.loginName + "/posts/count");

		assertEquals("{\"count\":2}", response.out.toString());
	}
	
	private UserRequest createUserRequest(String login, String pass){
		return createUserRequest(login, pass, null, null);
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
