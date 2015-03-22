package jobs;

import java.util.Date;
import java.util.Iterator;

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

@Every("60s")
public class InstagramsPullJob extends Job {
	public void doJob() {
		
		Logger.info("Running: InstagramsPullJob");
		
		String postsUrl = "https://api.instagram.com/v1/tags/zaibalo/media/recent?client_id=e3c2ae0fb7904532bfa625cb0f272e99";
		HttpResponse response = WS.url(postsUrl).get();
		
		JsonArray dataArray = response.getJson().getAsJsonObject().getAsJsonArray("data");
		Iterator<JsonElement> iterator = dataArray.iterator();
		while(iterator.hasNext()){
			JsonObject postJson = iterator.next().getAsJsonObject();
			
			String userPhoto = postJson.getAsJsonObject("user").getAsJsonPrimitive("profile_picture").getAsString();
			//String loginName = post.getAsJsonObject("user").getAsJsonPrimitive("username").getAsString();
			String displayName = postJson.getAsJsonObject("user").getAsJsonPrimitive("full_name").getAsString();
			long userId = postJson.getAsJsonObject("user").getAsJsonPrimitive("id").getAsLong();
			
			User user = User.findByLoginName(String.valueOf(userId));
			if(user == null){
				user = new User();
				user.displayName = displayName;
				user.loginName = String.valueOf(userId);
				user.photo = userPhoto;
				user.photoProvider = ServiceProvider.INSTAGRAM;
				user.save();
				Logger.info("User with display name: " + displayName + " created.");
			}		
			
			//String id = postJson.getAsJsonPrimitive("id").getAsString();
			long creationDateLong = postJson.getAsJsonPrimitive("created_time").getAsLong() * 1000;
			Date creationDate = new Date(creationDateLong);
			//String postLink = postJson.getAsJsonPrimitive("link").getAsString();
			String caption = postJson.getAsJsonObject("caption").getAsJsonPrimitive("text").getAsString();
			
			String content = "";
			
			String type = postJson.getAsJsonPrimitive("type").getAsString(); //image or video
			if(type.equals("image")){
				String imageUrl = postJson.getAsJsonObject("images").getAsJsonObject("standard_resolution").getAsJsonPrimitive("url").getAsString();
				content = "<img src=\"" + imageUrl + "\" class=\"center\" /><br>" + caption;
			} else if(type.equals("Video")){
				String videoUrl = postJson.getAsJsonObject("videos").getAsJsonObject("standard_resolution").getAsJsonPrimitive("url").getAsString();
				int width = postJson.getAsJsonObject("videos").getAsJsonObject("standard_resolution").getAsJsonPrimitive("width").getAsInt();
				int height = postJson.getAsJsonObject("videos").getAsJsonObject("standard_resolution").getAsJsonPrimitive("height").getAsInt();
				content = "<video width=\"" + width + "\" height=\"" + height + "\" controls><source src=\"" + videoUrl + "\" type=\"video/mp4\"></video><br>" + caption;
			}
			
			Post post = Post.find("byAuthorAndCreationDate", user, creationDate).first();
			if(post == null){
				post = new Post();
				post.author = user;
				post.creationDate = creationDate;
				post.content = content;
				post.save();
				Logger.info("Post with content: " + content + " created.");
			}
		}
    }
}
