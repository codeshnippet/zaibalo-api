package controllers;

import play.mvc.Controller;

public class Index extends Controller {

	public static void index() {
		renderTemplate("Index/posts.html");
	}
	
    public static void client() {
        render();
    }
}