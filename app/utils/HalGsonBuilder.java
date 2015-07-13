package utils;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalDeserializer;
import ch.halarious.core.HalExclusionStrategy;
import ch.halarious.core.HalResource;
import ch.halarious.core.HalSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HalGsonBuilder {

	public static Gson getDeserializerGson(Class clazz){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(HalResource.class, new HalDeserializer(clazz));
		builder.setExclusionStrategies(new HalExclusionStrategy());
		return builder.create();
	}

	public static Gson getSerializerGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(HalResource.class, new HalSerializer());
		builder.setExclusionStrategies(new HalExclusionStrategy());
		return builder.create();
	}
}
