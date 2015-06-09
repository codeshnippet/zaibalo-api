package utils;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalDeserializer;
import ch.halarious.core.HalExclusionStrategy;
import ch.halarious.core.HalResource;
import ch.halarious.core.HalSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HalGsonBuilder {
	private Gson gson;

	public HalGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(HalResource.class, new HalSerializer());
		builder.registerTypeAdapter(HalResource.class, new HalDeserializer(HalResource.class));
		builder.setExclusionStrategies(new HalExclusionStrategy());
		this.gson = builder.create();
	}

	public Gson getGson(){
		return this.gson;
	}
}
