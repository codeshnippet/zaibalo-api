package controllers;

import java.util.List;

import models.Post;
import play.db.jpa.JPABase;
import play.mvc.Controller;

public class Posts extends Controller {

    public static void create(String content) {
    	renderJSON(new Post(content).save());
    }
    
    public static void getAll() {
    	renderJSON(Post.findAll());
    }

}