package controllers.posts;

import java.io.InputStreamReader;
import java.util.List;

import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

import com.google.gson.GsonBuilder;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Posts extends Controller {

	public static void getPosts() {
		List<Post> postsList = Post.findAll();
		List<PostResponse> responseList = PostResponse.convertToJsonList(postsList);
		renderJSON(responseList);
	}

	@Secured
	public static void createPost() {
		User connected = Security.connected(request);
		
		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		Post post = new Post();
		post.content = postJSON.content;
		post.author = connected;
		post.save();
		
		response.setContentTypeIfNotSet("application/json");
		ok();
	}

	public static void getPost(long id) {
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		renderJSON(PostResponse.convertToJson(post));
	}

	public static void editPostPost(long id) {
		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		post.content = postJSON.content;
		post.save();
		response.setContentTypeIfNotSet("application/json");
		ok();
	}

	public static void deletePostPost(long id) {
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		post.delete();
		ok();
	}
}