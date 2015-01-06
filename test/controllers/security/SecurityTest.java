package controllers.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Util;
import play.test.Fixtures;
import controllers.BasicFunctionalTest;

public class SecurityTest extends BasicFunctionalTest {

	private static final String DEFAULT_PATH = "/controllers.SecurityTestController/secureAction";
	
	private static final String USER_PASSWORD = "secret";
	private static final String USER_NAME = "franky";
	
	private static final String POST = "POST";
	private static final String DEFAULT_BODY = "testbody";
	private static final String DEFAULT_BODY_MD5 = DigestUtils.md5Hex(DEFAULT_BODY);
	private static final String APPLICATION_JSON = "application/json";

	@Before
	public void beforeTest() {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testSecureActionIsSecure() {
		Response response = POST(DEFAULT_PATH);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionSuccess() {
		Fixtures.loadModels("data/user.yml");

		String currentTimeMillis = String.valueOf(System.currentTimeMillis());
		Request request = createAuthRequestForTimestamp(currentTimeMillis);

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertIsOk(response);
		assertContentEquals("This text is a secret!", response);
	}
	
	@Test
	public void testSecureActionFailsWithWrongUsername() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForUsername("wrong_username");

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertStatus(401, response);
	}
	
	@Test
	public void testSecureActionFailsWithWrongPassword() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForPassword("wrong_password");

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithElevenMinutesOldTimestamp() {
		Fixtures.loadModels("data/user.yml");

		String currentTimeMillis = String.valueOf(System.currentTimeMillis() - 11 * 60 * 1000);
		Request request = createAuthRequestForTimestamp(currentTimeMillis);

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionSuccessWithNineMinutesOldTimestamp() {
		Fixtures.loadModels("data/user.yml");

		String currentTimeMillis = String.valueOf(System.currentTimeMillis() - 9 * 60 * 1000);
		Request request = createAuthRequestForTimestamp(currentTimeMillis);

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertIsOk(response);
		assertContentEquals("This text is a secret!", response);
	}

	@Test
	public void testSecureActionFailsWithDifferentContentType() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForContentType(APPLICATION_JSON);

		Response response = POST(request, DEFAULT_PATH, "application/xml", DEFAULT_BODY);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithDifferentMethod() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForMethod(POST);

		Response response = PUT(request, DEFAULT_PATH, APPLICATION_JSON, DEFAULT_BODY);
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithDifferentBody() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForBody(DEFAULT_BODY);

		Response response = POST(request, DEFAULT_PATH, APPLICATION_JSON, "OtherBody");
		assertStatus(401, response);
	}

	@Test
	public void testSecureActionFailsWithDifferentPath() {
		Fixtures.loadModels("data/user.yml");

		Request request = createAuthRequestForPath(DEFAULT_PATH);

		Response response = POST(request, "/controllers.SecurityTestController/otherSecureAction", APPLICATION_JSON, DEFAULT_BODY);
		assertStatus(401, response);
	}
	
	private Request createAuthRequestForUsername(String username) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, null, null, null, null);
		return createAuthRequest(timestamp, data, username, USER_PASSWORD);
	}
	
	private Request createAuthRequestForPassword(String passord) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, null, null, null, null);
		return createAuthRequest(timestamp, data, USER_NAME, passord);
	}

	private Request createAuthRequestForBody(String body) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, null, null, DigestUtils.md5Hex(body), null);
		return createAuthRequest(timestamp, data);
	}

	private Request createAuthRequestForMethod(String method) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, null, null, null, method);
		return createAuthRequest(timestamp, data);
	}

	private Request createAuthRequestForTimestamp(String timestamp) {
		String data = createDataString(timestamp, null, null, null, null);
		return createAuthRequest(timestamp, data);
	}

	private Request createAuthRequestForContentType(String contentType) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, null, contentType, null, null);
		return createAuthRequest(timestamp, data);
	}

	private Request createAuthRequestForPath(String path) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = createDataString(timestamp, DEFAULT_PATH + path, null, null, null);
		return createAuthRequest(timestamp, data);
	}

	private String createDataString(String timestamp, String pathParam, String contentTypeParam, String bodyMd5HexParam, String methodParam) {
		String path = pathParam == null ? DEFAULT_PATH : pathParam;
		String contentType = contentTypeParam == null ? APPLICATION_JSON : contentTypeParam;
		String bodyMd5Hex = bodyMd5HexParam == null ? DEFAULT_BODY_MD5 : bodyMd5HexParam;
		String method = methodParam == null ? POST : methodParam;
		return concatDataString(timestamp, path, contentType, bodyMd5Hex, method);
	}

	private Request createAuthRequest(String timestamp, String data) {
		return createAuthRequest(timestamp, data, USER_NAME, USER_PASSWORD);
	}

}
