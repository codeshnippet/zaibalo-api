package controllers;

import java.io.IOException;

import play.mvc.Controller;
import play.mvc.Util;

public class BasicController extends Controller {
    
    @Util
    protected static void writeToResponseBody(String text) {
        try {
            response.out.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
