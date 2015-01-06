package controllers.posts;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;

import models.Post;
import models.User;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.With;

import com.google.gson.GsonBuilder;
import play.Logger;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Posts extends Controller {

	public static void getPosts(String sort, int page, int limit) {
		JPAQuery postsQuery = null;
		if("created_at".equals(sort)){
			postsQuery = Post.find("order by creationDate desc");
		}else{
			postsQuery = Post.all();
		}
		limit = (limit == 0) ? 10 : limit; 
		List<Post> postsList = postsQuery.fetch(page, limit);
		List<PostResponse> responseList = PostResponse.convertToPostResponsesList(postsList);
		renderJSON(responseList);
	}
	
	public static void getPostsCount(){
		renderJSON("{\"count\":" + Post.count() +"}");
	}

	@Secured
	public static void createPost() {
		User connected = Security.getAuthenticatedUser();

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
		renderJSON(PostResponse.convertToPostResponse(post));
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
		if(post.author.id != Security.getAuthenticatedUser().id){
			forbidden();
		}
		post.content = postJSON.content;
		post.save();
		
		response.setContentTypeIfNotSet("application/json");
		renderJSON(PostResponse.convertToPostResponse(post));
	}

	@Secured
	public static void deletePost(long id) {
		User connected = Security.getAuthenticatedUser();
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