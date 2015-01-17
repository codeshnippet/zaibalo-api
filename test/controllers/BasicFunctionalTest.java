package controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Util;
import play.test.FunctionalTest;

public abstract class BasicFunctionalTest extends FunctionalTest {

	protected static final String AUTHENTICATION_HEADER = "Authorization";
	public static final String TIMESTAMP_HEADER_NAME = "x-utc-timestamp";

	@Util
	protected Request getAuthRequest(String url, String contentType, String body, String method) {
		return getAuthRequest(url, contentType, body, method, "franky", "secret");
	}
	
	@Util
	protected Request getAuthRequestForDefaultUser(String url, String method) {
		return getAuthRequest(url, "text/html", "", method, "franky", "secret");
	}
	
	@Util
	protected Request getAuthRequestForUser(String url, String method, String username, String password) {
		return getAuthRequest(url, "text/html", "", method, username, password);
	}
	
	@Util
	protected Request getAuthRequest(String path, String contentType, String body, String method, String username, String password) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String data = concatDataString(timestamp, path, contentType, DigestUtils.md5Hex(body), method);
		return createAuthRequest(timestamp, data, username, password);
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
	protected Request createAuthRequest(String timestamp, String data, String username, String password) {
		Request request = newRequest();
		request.user = username;
		request.headers.put(TIMESTAMP_HEADER_NAME, new Header(TIMESTAMP_HEADER_NAME, timestamp));
		request.password = sha1(data, DigestUtils.md5Hex(password));
		return request;
	}

	@Util
	protected String concatDataString(String timestamp, String path, String contentType, String bodyMd5Hex, String method) {
		return method + "\n" + bodyMd5Hex + "\n" + contentType + "\n" + timestamp + "\n" + path;
	}
}
