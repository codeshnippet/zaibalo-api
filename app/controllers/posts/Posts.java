package controllers.posts;

import java.io.InputStreamReader;
import java.util.List;

import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Header;

import com.google.gson.GsonBuilder;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Posts extends Controller {

	public static void getPosts() {
		List<Post> postsList = Post.findAll();
		List<PostResponse> responseList = PostResponse.convertToPostResponsesList(postsList);
		renderJSON(responseList);
	}

	public static void createPost() {
		User connected = Security.getAuthenticatedOrAnonymousUser(request);
		
		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		Post post = new Post();
		post.content = postJSON.content;
		post.author = connected;
		post.save();
		
		String location = request.host + "/posts/" + post.id;
		response.headers.put("Location", new Header("Location", location));
		response.setContentTypeIfNotSet("application/json");
		response.status = 201;
	}

	public static void getPost(long id) {
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		renderJSON(PostResponse.convertToPostResponse(post));
	}

	@Secured
	public static void editPost(long id) {
		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		if(post.author.id != Security.getAuthenticatedUser(request).id){
			forbidden();
		}
		post.content = postJSON.content;
		post.save();
		
		response.setContentTypeIfNotSet("application/json");
		renderJSON(PostResponse.convertToPostResponse(post));
	}

	@Secured
	public static void deletePost(long id) {
		User connected = Security.getAuthenticatedUser(request);
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		if(post.author.id != connected.id){
			forbidden();
		}
		post._delete();
		ok();
	}
}