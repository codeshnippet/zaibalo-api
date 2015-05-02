package controllers.posts;

import java.util.ArrayList;
import java.util.List;

import models.PostAttachment;

public class PostAttachmentResponse {
	public String url;
	public String type;
	
	public static List<PostAttachmentResponse> convertToPostAttachmentListResponse(List<PostAttachment>  attachmentsList){
		List<PostAttachmentResponse> responseList = new ArrayList<PostAttachmentResponse>();
		for(PostAttachment attachment: attachmentsList){
			responseList.add(convertToPostAttachmentResponse(attachment));
		}
		return responseList;
	}
	
	private static PostAttachmentResponse convertToPostAttachmentResponse(PostAttachment  attachment){
		PostAttachmentResponse response = new PostAttachmentResponse();
		response.url = attachment.url;
		response.type = attachment.type.toString();
		return response;
	}
}
