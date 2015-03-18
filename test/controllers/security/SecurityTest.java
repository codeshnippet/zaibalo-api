package controllers.security;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import controllers.ContentType;
import controllers.HttpMethod;
import controllers.RequestBuilder;

public class SecurityTest extends FunctionalTest {

	private static final String DEFAULT_PATH = "/controllers.SecurityTestController/secureAction";

	private static final String DEFAULT_BODY = "testbody";

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data/user.yml");
	}

	@Test
	public void testSecureActionIsSecure() {
		Response response = POST(DEFAULT_PATH);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionSuccess() {
		Response response = new RequestBuilder()
		.withPath(DEFAULT_PATH)
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(DEFAULT_BODY)
		.send();

		assertIsOk(response);
		assertContentEquals("This text is a secret!", response);
	}

	@Test
	public void testSecureActionFailsWithWrongUsername() {
		Response response = new RequestBuilder()
		.withPath(DEFAULT_PATH)
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(DEFAULT_BODY)
		.withUsername("wrong_username")
		.send();

		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithWrongToken() {
		Response response = new RequestBuilder()
		.withPath(DEFAULT_PATH)
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(DEFAULT_BODY)
		.withToken("wrong_token")
		.send();

		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithElevenMinutesOldTimestamp() {
		Response response = new RequestBuilder()
		.withPath(DEFAULT_PATH)
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(DEFAULT_BODY)
		.withTimestamp(System.currentTimeMillis() - 11 * 60 * 1000)
		.send();

		assertStatus(401, response);
	}

	@Test
	public void testSecureActionSuccessWithNineMinutesOldTimestamp() {
		Response response = new RequestBuilder()
		.withPath(DEFAULT_PATH)
		.withHttpMethod(HttpMethod.POST)
		.withContentType(ContentType.APPLICATION_JSON)
		.withBody(DEFAULT_BODY)
		.withTimestamp(System.currentTimeMillis() - 9 * 60 * 1000)
		.send();

		assertIsOk(response);
		assertContentEquals("This text is a secret!", response);
	}

}
