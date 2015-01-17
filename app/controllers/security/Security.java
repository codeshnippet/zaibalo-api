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
import play.mvc.Util;

public class Security extends Controller {

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
			
			String token = null;
			try {
				token = createHmac1Token(user.getPassword(), timestampHeader.value());
			} catch (Exception e) {
				e.printStackTrace();
				error("Unable to verify auth token.");
			}
			
			if(!token.equals(request.password)){
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
	private static String createHmac1Token(String passwordHash, String timestampValue) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String data = request.method + "\n" +
				DigestUtils.md5Hex(readRequestBody()) + "\n" +
				request.contentType + "\n" +
				timestampValue + "\n" +
				request.path + request.querystring;
		return sha1(passwordHash, data);
	}

	@Util
	public static User getAuthenticatedUser() {
		return User.findByLoginName(request.user);
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
