package controllers.posts;

import java.io.InputStreamReader;
import java.util.List;

import models.Post;
import models.User;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Util;
import play.mvc.With;
import utils.HalGsonBuilder;
import ch.halarious.core.HalSerializer;

import com.google.gson.GsonBuilder;

import controllers.security.Secured;
import controllers.security.Security;

@With(Security.class)
public class Posts extends Controller {

	public static void getPostsByTag(String name, String sort, int from, int limit) {
		renderPostsListJson(sort, from, limit, "content like ?1", "%#" + name + "%");
	}
	
	public static void getPosts(String sort, int from, int limit) {
		renderPostsListJson(sort, from, limit, "");
	}

	public static void getPostsCount(){
		long count = Post.count();
		renderCountJson(count);
	}

	public static void getPostsByTagCount(String name){
		long count = Post.count("content like ?1", "%#" + name + "%");
		renderCountJson(count);
	}
	
	@Secured
	public static void createPost() {
		User user = Security.getAuthenticatedUser();

		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		
		if(!postJSON.isValid()){
			badRequest();
		}
		
		Post post = new Post();
		post.content = postJSON.content;
		post.author = user;
		post.save();

		String location = request.host + "/posts/" + post.id;
		response.headers.put("Location", new Header("Location", location));
		response.setContentTypeIfNotSet("application/json");
		response.status = 201;
		renderJSON(PostResponse.convertToPostResponse(post, user));
	}

	public static void getPost(long id) {
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}
		User user = Security.getAuthenticatedUser();

		renderJSON(PostResponse.convertToPostResponse(post, user));
	}

	@Secured
	public static void editPost(long id) {
		PostRequest postJSON = new GsonBuilder().create().
				fromJson(new InputStreamReader(request.body), PostRequest.class);
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}

		Security.verifyOwner(post.author);

		post.content = postJSON.content;
		post.save();

		response.setContentTypeIfNotSet("application/json");
		User user = Security.getAuthenticatedUser();
		renderJSON(PostResponse.convertToPostResponse(post, user));
	}

	@Secured
	public static void deletePost(long id) {
		Post post = Post.findById(id);
		if (post == null) {
			notFound();
		}

		Security.verifyOwner(post.author);

		post.delete();
		ok();
	}
	
	@Util
	public static void renderPostsListJson(String sort, int from, int limit, String query, Object... params) {
		if("created_at".equals(sort)){
			query += " order by creationDate desc";
		}
		JPAQuery postsQuery = Post.find(query, params);
		
		limit = (limit == 0) ? 10 : limit;
		List<Post> postsList = postsQuery.from(from).fetch(limit);
		User user = Security.getAuthenticatedUser();
		renderJSON(PostResponse.convertToPostResponsesList(postsList, user));
	}

	@Util
	public static void renderCountJson(long count) {
		renderJSON("{\"count\":" + count +"}");
	}
}
