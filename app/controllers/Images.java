package controllers;

import java.io.File;

import play.Play;
import play.mvc.Controller;

public class Images extends Controller{
	public static final String IMAGE_DIR_PATH = Play.configuration.getProperty("image.dir.path");
	
	public static void image(String fileName) {
		File image = new File(IMAGE_DIR_PATH + fileName);
	    renderBinary(image);
	}

}
