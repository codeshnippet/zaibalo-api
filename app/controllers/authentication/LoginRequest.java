package controllers.authentication;

import org.apache.commons.lang.StringUtils;

public class LoginRequest {

	public String loginName;
	public String password;
	
	public LoginRequest(String loginName, String password){
		this.loginName = loginName;
		this.password = password;
	}
}
