package controllers;

public enum HttpMethod {
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE"),
    GET("GET");
	private String text;

	HttpMethod(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
