package controllers;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Util;
import play.test.FunctionalTest;

public abstract class BasicFunctionalTest extends FunctionalTest {

	protected static final String USER_TOKEN = "secret_token_123";
	protected static final String USER_NAME = "franky";
	
	private static final String TIMESTAMP_HEADER_NAME = "x-utc-timestamp";
	private static final String X_AUTH_USERNAME_HEADER_NAME = "x-auth-username";
	private static final String X_AUTH_TOKEN_HEADER_NAME = "x-auth-token";
	
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "content-type";

	@Util
	protected Request getAuthRequest(String url, String contentType, String body, String method) {
		return getAuthRequest(url, contentType, body, method, USER_NAME, USER_TOKEN);
	}

	@Util
	protected Request getAuthRequest(String path, String contentType, String body, String method, String username, String token) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = concatDataString(timestamp, path, contentType, DigestUtils.md5Hex(body), method);
		return createAuthRequest(timestamp, data, username, token, APPLICATION_JSON);
	}
	
	@Util
	protected String sha1(String data, String keyString){
		byte[] bytes = new byte[0];
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
			bytes = mac.doFinal(data.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(bytes));
	}

	@Util
	protected Request createAuthRequest(String timestamp, String data, String username, String token, String contentType) {
		Request request = newRequest();
		request.headers.put(X_AUTH_USERNAME_HEADER_NAME, new Header(X_AUTH_USERNAME_HEADER_NAME, username));
		request.headers.put(TIMESTAMP_HEADER_NAME, new Header(TIMESTAMP_HEADER_NAME, timestamp));
		if(contentType != null){
			request.headers.put(CONTENT_TYPE, new Header(CONTENT_TYPE, contentType));
		}
		String authToken = sha1(data, token);
		request.headers.put(X_AUTH_TOKEN_HEADER_NAME, new Header(X_AUTH_TOKEN_HEADER_NAME, authToken));
		return request;
	}

	@Util
	protected String concatDataString(String timestamp, String path, String contentType, String bodyMd5Hex, String method) {
		String data = method + "\n" + bodyMd5Hex + "\n" + contentType + "\n" + timestamp + "\n" + path;
		return data.toLowerCase();
	}
}
