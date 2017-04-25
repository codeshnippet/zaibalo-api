package controllers.redirects;

import controllers.BasicController;
import models.Post;
import models.Tag;
import models.User;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acidum on 4/25/17.
 */
public class Redirects extends BasicController {
    public static void userRedirect(Long id) {
        User user = User.findById(id);
        if(user == null){
            redirect("/#/");
        } else {
            redirect("/#/@" + user.loginName);
        }
    }

    public static void postRedirect(Long id){
        Post post = Post.findById(id);
        if(post == null){
            redirect("/#/");
        } else {
            redirect("/#/post/" + post.id);
        }
    }

    public static void categoryRedirect(Long id) throws UnsupportedEncodingException {
        Tag tag = Tag.findById(id);
        if(tag == null){
            redirect("/#/");
        } else {
            String tagName = tag.name.substring(1);
            redirect("/#/tag/" + URLEncoder.encode(tagName, "UTF-8"));
        }
    }
}
