package controllers;

import play.mvc.Controller;
import play.mvc.With;
import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class SecurityTestController extends Controller{

	@Secured
	public static void secureAction(){
		renderText("This text is a secret!");
	}

	@Secured
	public static void otherSecureAction(){
		renderText("This text is a secret!");
	}
	
}
