package controllers.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import models.User;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Util;

public class Security extends Controller {

	private static final String CONTENT_TYPE = "content-type";
	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final String UTF_8 = "UTF-8";

	@Before
	public static void securityCheck() {
		Secured secured = getActionAnnotation(Secured.class);
		if (secured != null) {
			User user = getAuthenticatedUser();
			if(user == null) {
				unauthorized();
			}
			
			Header timestampHeader = request.headers.get("x-utc-timestamp");
			if(timestampHeader == null){
				unauthorized();
			}
			
			if(System.currentTimeMillis() - Long.valueOf(timestampHeader.value()) > 10*60*1000){
				unauthorized();
			}
			
			String hmacToken = null;
			try {
				hmacToken = createHmac1Token(user.token, timestampHeader.value());
			} catch (Exception e) {
				e.printStackTrace();
				error("Unable to verify auth token.");
			}
			
			Header authTokenHeader = request.headers.get("x-auth-token");
			if(authTokenHeader == null){
				unauthorized();
			}
			
			if(!hmacToken.equals(authTokenHeader.value())){
				unauthorized();
			}
		}
	}

	@Util
	public static void verifyOwner(User user){
		if(getAuthenticatedUser().id != user.id){
			forbidden();
		}
	}
	
	@Util
	private static String createHmac1Token(String token, String timestampValue) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		Header contentType = request.headers.get(CONTENT_TYPE);
		String contentTypeValue = contentType == null ? "" : contentType.value();
		String data = request.method + "\n" +
				DigestUtils.md5Hex(readRequestBody()) + "\n" +
				contentTypeValue + "\n" +
				timestampValue + "\n" +
				request.path + request.querystring;
		return sha1(token, data.toLowerCase());
	}

	@Util
	public static User getAuthenticatedUser() {
		Header usernameHeader = request.headers.get("x-auth-username");
		if(usernameHeader == null){
			return null;
		}
		return User.findByLoginName(usernameHeader.value());
	}

	@Util
	private static String sha1(String key, String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec((key).getBytes(UTF_8), HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(secretKeySpec);
		byte[] bytes = mac.doFinal(data.getBytes(UTF_8));
		return new String(Base64.encodeBase64(bytes));
	}
	
	@Util
	private static byte[] readRequestBody() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(request.body, baos);
		byte[] bytes = baos.toByteArray();
		request.body = new ByteArrayInputStream(bytes);
		return bytes;
	}
}
