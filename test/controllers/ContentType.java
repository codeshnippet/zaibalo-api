package controllers;

public enum ContentType {
	APPLICATION_JSON("application/json"), TEXT_HTML("text/html");
	private String text;

	ContentType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
