package controllers.posts;

import ch.halarious.core.HalResource;
import com.google.gson.Gson;
import org.junit.Before;
import play.mvc.Http;
import play.test.Fixtures;
import play.test.FunctionalTest;
import utils.HalGsonBuilder;

import java.util.List;

public abstract class AbstractPostsTest extends FunctionalTest{

    @Before
    public void beforeTest() {
        Fixtures.deleteAllModels();
    }

    protected PostsListResource getPostsListResource(Http.Response response) {
        Gson gson = HalGsonBuilder.getDeserializerGson(PostsListResource.class);
        String responseBody = response.out.toString();
        return (PostsListResource) gson.fromJson(responseBody, HalResource.class);
    }

    protected List<PostResource> getPostsListFrom(Http.Response response) {
        PostsListResource postsListResource = getPostsListResource(response);
        return postsListResource.posts;
    }

    protected  PostResource getPostFrom(Http.Response response){
        Gson gson = HalGsonBuilder.getDeserializerGson(PostResource.class);
        String responseBody = response.out.toString();
        return (PostResource) gson.fromJson(responseBody, HalResource.class);
    }
}
