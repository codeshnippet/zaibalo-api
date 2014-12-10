package controllers.posts;

public class PostRequest {

	public PostRequest(){
	}
	
	public PostRequest(String content){
		this.content = content;
	}
	
	public String content;
}
