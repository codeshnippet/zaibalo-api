package controllers;

import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Util;
import play.test.FunctionalTest;

public abstract class BasicFunctionalTest extends FunctionalTest {

	protected static final String AUTHENTICATION_HEADER = "Authorization";

	@Util
	protected Request getAuthRequest() {
		return getAuthRequest("franky", "secret");
	}
	
	@Util
	protected Request getAuthRequest(String username, String password) {
		Request request = newRequest();
		request.user = username;
        request.password = password;
		return request;
	}
}
