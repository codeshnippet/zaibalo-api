package controllers;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Util;
import play.test.FunctionalTest;


public class RequestBuilder {
	
	private static final String TIMESTAMP_HEADER_NAME = "x-utc-timestamp";
	private static final String X_AUTH_USERNAME_HEADER_NAME = "x-auth-username";
	private static final String X_AUTH_TOKEN_HEADER_NAME = "x-auth-token";
	private static final String CONTENT_TYPE = "content-type";

	private String path;
	private ContentType contentType = ContentType.TEXT_HTML;
	private String body = StringUtils.EMPTY;
	private HttpMethod httpMethod;
	private String username = "franky";
	private String token = "secret_token_123";
	private long timestamp = System.currentTimeMillis();

	@Util
	public Response send() {
		validate();
		switch (httpMethod) {
		case POST:
			return sendPost();
		case PUT:
			return sendPut();
		case DELETE:
			return sendDelete();
		}
		throw new RuntimeException("Should never happen");
	}
	
	private Response sendDelete() {
		return FunctionalTest.DELETE(createAuthRequest(), path);
	}

	private Response sendPut() {
		return FunctionalTest.PUT(createAuthRequest(), path, contentType.getText(), body);
	}

	private Response sendPost() {
		return FunctionalTest.POST(createAuthRequest(), path, contentType.getText(), body);
	}

	private void validate() {
		if (path == null) {
			throw new RuntimeException("Path cannot be null");
		}
		if (httpMethod == null) {
			throw new RuntimeException("HttpMethod cannot be null");
		}
	}

	@Util
	public RequestBuilder withPath(String path) {
		this.path = path;
		return this;
	}

	@Util
	public RequestBuilder withContentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}

	@Util
	public RequestBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	@Util
	public RequestBuilder withHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	@Util
	public RequestBuilder withUsername(String username) {
		this.username = username;
		return this;
	}

	@Util
	public RequestBuilder withToken(String token) {
		this.token = token;
		return this;
	}
	
	@Util
	public RequestBuilder withTimestamp(long timestamp){
		this.timestamp = timestamp;
		return this;
	}
	
	private Request createAuthRequest() {
		Request request = FunctionalTest.newRequest();
		request.headers.put(TIMESTAMP_HEADER_NAME, new Header(TIMESTAMP_HEADER_NAME, String.valueOf(timestamp)));
		request.headers.put(CONTENT_TYPE, new Header(CONTENT_TYPE, contentType.getText()));
		
		String data = concatDataString(String.valueOf(timestamp), path, contentType.getText(), DigestUtils.md5Hex(body), httpMethod.getText());
		request.headers.put(X_AUTH_USERNAME_HEADER_NAME, new Header(X_AUTH_USERNAME_HEADER_NAME, username));
		request.headers.put(X_AUTH_TOKEN_HEADER_NAME, new Header(X_AUTH_TOKEN_HEADER_NAME, sha1(data, token)));
		
		return request;
	}
	
	private String concatDataString(String timestamp, String path, String contentType, String bodyMd5Hex, String method) {
		String data = method + "\n" + bodyMd5Hex + "\n" + contentType + "\n" + timestamp + "\n" + path;
		return data.toLowerCase();
	}
	
	private String sha1(String data, String keyString){
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
}