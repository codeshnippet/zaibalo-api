package controllers;

import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalResource;
import com.google.gson.reflect.TypeToken;
import play.mvc.Controller;
import play.mvc.Util;
import utils.HalGsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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
    protected static String convertToHalResponse(HalResource halResource) {
        return new HalGsonBuilder().getSerializerGson().toJson(halResource, HalResource.class);
    }

    @Util
    protected static String convertToHalListResponse(List<? extends HalBaseResource> halResourcesList) {
        Type type = new TypeToken<List<HalResource>>() {
        }.getType();
        return new HalGsonBuilder().getSerializerGson().toJson(halResourcesList, type);
    }
}
