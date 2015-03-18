package controllers.security;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import com.google.gson.Gson;

public class LoginTest extends FunctionalTest {
	
	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data/user.yml");
	}
	
	@Test
	public void testLoginSuccess(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("franky", "secret")));
		assertStatus(200, response);
		assertContentType("application/json", response);
		assertContentMatch("\\{.*Superman.*secret_token_123.*\\}", response);
	}
	
	@Test
	public void testLoginWrongUsername(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("wrong_username", "secret")));
		assertStatus(400, response);
	}
	
	@Test
	public void testLoginWrongPassword(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("franky", "wrong_password")));
		assertStatus(400, response);
	}
	
	@Test
	public void testLoginEmptyUsername(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("", "secret")));
		assertStatus(400, response);
	}
	
	@Test
	public void testLoginEmptyPassword(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("franky", "")));
		assertStatus(400, response);
	}
	
	@Test
	public void testLoginEmptyUsernameAndPassword(){
		Response response = POST("/login", "application/json", toJson(new LoginRequest("", "")));
		assertStatus(400, response);
	}
	private String toJson(Object object){
		return new Gson().toJson(object);
	}
}
