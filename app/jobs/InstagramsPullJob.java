package jobs;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import models.Post;
import models.ServiceProvider;
import models.User;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Every("15mn")
public class InstagramsPullJob extends Job {
	private static final String INSTAGRAM_API_URL = "https://api.instagram.com/v1/tags/%D0%B7%D0%B0%D1%97%D0%B1%D0%B0%D0%BB%D0%BE/media/recent?client_id=e3c2ae0fb7904532bfa625cb0f272e99";

	public void doJob() {
		
		HttpResponse response = WS.url(INSTAGRAM_API_URL).get();
		
		Iterator<JsonElement> iterator = response.getJson().getAsJsonObject().getAsJsonArray("data").iterator();
		while(iterator.hasNext()){
			JsonObject postJson = iterator.next().getAsJsonObject();

			if(!isMediaTypeSupported(postJson)){
				continue;
			}
			
			long userId = getUserId(postJson);
			String userPhoto = getUserPhoto(postJson);
			String displayName = getDisplayName(postJson);
			Date creationDate = getCreationDate(postJson);
			String content = buildContent(postJson);
			
			try{
				User user = createUserIfNotExists(userId, userPhoto, displayName);
				createPostIfNotExists(user, creationDate, content);
			} catch(Exception ex){
				Logger.warn("Post could not be fetched from instagram.");
			}
		}
    }

	private boolean isMediaTypeSupported(JsonObject postJson) {
		String type = postJson.getAsJsonPrimitive("type").getAsString();
		return type.equals("image");
	}

	private Date getCreationDate(JsonObject postJson) {
		long creationDateLong = postJson.getAsJsonPrimitive("created_time").getAsLong() * 1000;
		return new Date(creationDateLong);
	}

	private String getDisplayName(JsonObject postJson) {
		String loginName = postJson.getAsJsonObject("user").getAsJsonPrimitive("username").getAsString();
		String displayName = postJson.getAsJsonObject("user").getAsJsonPrimitive("full_name").getAsString();
		displayName = StringUtils.isEmpty(displayName) ? loginName : displayName;
		return displayName;
	}

	private String getUserPhoto(JsonObject postJson) {
		return postJson.getAsJsonObject("user").getAsJsonPrimitive("profile_picture").getAsString();
	}

	private long getUserId(JsonObject postJson) {
		return postJson.getAsJsonObject("user").getAsJsonPrimitive("id").getAsLong();
	}

	private String buildContent(JsonObject postJson) {
		String caption = postJson.getAsJsonObject("caption").getAsJsonPrimitive("text").getAsString();
		String imageUrl = postJson.getAsJsonObject("images").getAsJsonObject("standard_resolution").getAsJsonPrimitive("url").getAsString();
		return "<img src=\"" + imageUrl + "\" class=\"center\" /><br>" + caption;
	}

	private void createPostIfNotExists(User user, Date creationDate, String content) {
		Post post = Post.find("byAuthorAndCreationDate", user, creationDate).first();
		if(post == null){
			post = new Post();
			post.author = user;
			post.creationDate = creationDate;
			post.content = content;
			post.save();
		}
	}

	private User createUserIfNotExists(long userId, String userPhoto, String displayName) {
		User user = User.findByLoginName(String.valueOf(userId));
		if(user == null){
			user = new User(String.valueOf(userId), displayName);
			user.setPhoto(userPhoto);
			user.photoProvider = ServiceProvider.INSTAGRAM;
			user.save();
		}
		return user;
	}
}
