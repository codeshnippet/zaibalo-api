package controllers.redirects;

import controllers.BasicController;
import models.Post;
import models.Tag;
import models.User;
import org.apache.commons.codec.binary.Base64;
import play.mvc.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acidum on 4/25/17.
 */
public class Redirects extends BasicController {

    public static void loginRedirect() {
        redirect("/#/signup");
    }

    public static void feedRedirect() {
        redirect("/#/");
    }

    public static void userRedirect(Long id) {
        if(id == null){
            redirect("/#/");
        }

        User user = User.findById(id);
        if(user == null){
            redirect("/#/");
        } else {
            redirect("/#/@" + user.loginName);
        }
    }

    public static void userWithoutIdRedirect() {
        redirect("/#/");
    }

    public static void postRedirect(Long id){
        if(id == null){
            redirect("/#/");
        }

        Post post = Post.findById(id);
        if(post == null){
            redirect("/#/");
        } else {
            redirect("/#/post/" + post.id);
        }
    }

    public static void postWithoutIdRedirect(){
        redirect("/#/");
    }

    public static void categoryRedirect(Long id) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryWithoutIdRedirect(){
        redirect("/#/");
    }

    public static void categoryPageRedirect(Long id, Long page) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryCountRedirect(Long id, Long count) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryOrderCountPageRedirect(Long id, String order, Long count, Long page) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryOrderCountRedirect(Long id, String order, Long count) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }
    public static void categoryOrderPageRedirect(Long id, String order, Long page) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryOrderRedirect(Long id, String order) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void categoryCountPageRedirect(Long id, Long count, Long page) throws UnsupportedEncodingException {
        categoryByIdRedirect(id);
    }

    public static void countPageRedirect(Long count, Long page) {
        redirect("/#/");
    }

    public static void pageRedirect(Long page) {
        redirect("/#/");
    }

    public static void orderCountPageRedirect(String order, Long count, Long page) {
        redirect("/#/");
    }

    public static void orderCountRedirect(String order, Long count) {
        redirect("/#/");
    }

    public static void orderPageRedirect(String order, Long page) {
        redirect("/#/");
    }

    public static void orderRedirect(String order) {
        redirect("/#/");
    }

    @Util
    private static void categoryByIdRedirect(Long id) throws UnsupportedEncodingException {
        if(id == null){
            redirect("/#/");
        }

        Tag tag = Tag.findById(id);
        if(tag == null){
            redirect("/#/");
        } else {
            String tagName = tag.name.substring(1);
            redirect("/#/tag/" + URLEncoder.encode(tagName, "UTF-8"));
        }
    }

}
