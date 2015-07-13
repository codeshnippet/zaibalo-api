package controllers.posts;

import java.util.ArrayList;
import java.util.List;

import models.Post;
import models.User;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;

public class PostsListResource extends HalBaseResource {
	
	@HalEmbedded
	public List<PostResource> posts = new ArrayList<PostResource>();
	
	@HalLink
	public String add;
	
	public static PostsListResource convertToPostsListResource(List<Post> postsList, User user) {
		PostsListResource resource = new PostsListResource();
		resource.posts = convertToPostResponsesList(postsList, user);
		return resource;
	}
	
	private static List<PostResource> convertToPostResponsesList(List<Post> postsList, User user) {
		List<PostResource> postResourceList = new ArrayList<PostResource>(postsList.size());
		for (Post post : postsList) {
			PostResource postResponseJSON = PostResource.convertSinglePostResponse(post, user);
			postResourceList.add(postResponseJSON);
		}
		return postResourceList;
	}
}
