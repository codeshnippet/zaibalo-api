package controllers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import models.Post;
import models.User;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalResource;
import play.mvc.Controller;
import play.mvc.Util;
import utils.HalGsonBuilder;

public class BasicController extends Controller {
    
    @Util
    protected static void writeToResponseBody(String text) {
        try {
            response.out.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Util
	protected static String convertToHalResponse(HalBaseResource halResource) {
		return new HalGsonBuilder().getGson().toJson(halResource, HalResource.class);
	}
    
    @Util
	protected static String convertToHalListResponse(List<? extends HalBaseResource> halResourcesList) {
    	Type type = new TypeToken<List<HalResource>>() {}.getType();
		return new HalGsonBuilder().getGson().toJson(halResourcesList, type);
    }
}
