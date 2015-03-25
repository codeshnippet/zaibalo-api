package controllers.comments;

import org.apache.commons.lang.StringUtils;


public class CommentRequest {

	public String content;

	public boolean isValid() {
		return StringUtils.isNotEmpty(this.content);
	}
}
