package controllers.posts;


import com.google.common.base.Optional;
import controllers.security.Security;
import models.Post;
import models.User;
import ch.halarious.core.HalBaseResource;
import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import play.mvc.Router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsListResource extends HalBaseResource {

	@HalEmbedded
	public List<PostResource> posts = new ArrayList<PostResource>();

	@HalLink
	public String addPost;

    @HalLink
    public String next;

	public static PostsListResource convertToPostsListResource(List<Post> postsList, Optional<String> addPost, Optional<String> next) {
		PostsListResource resource = new PostsListResource();

		resource.posts = convertToPostResponsesList(postsList);

		if (addPost.isPresent()) {
			resource.addPost = addPost.get();
		}
        if (next.isPresent()) {
            resource.next = next.get();
        }
		return resource;
	}

	private static List<PostResource> convertToPostResponsesList(List<Post> postsList) {
		List<PostResource> postResourceList = new ArrayList<PostResource>(postsList.size());
		for (Post post : postsList) {
			PostResource postResponseJSON = PostResource.convertSinglePostResponse(post);
			postResourceList.add(postResponseJSON);
		}
		return postResourceList;
	}
}
