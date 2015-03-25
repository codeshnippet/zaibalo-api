package controllers.posts;

import org.apache.commons.lang.StringUtils;

public class PostRequest {

	public PostRequest(String content){
		this.content = content;
	}
	
	public String content;

	public boolean isValid() {
		return StringUtils.isNotEmpty(this.content);
	}
}
