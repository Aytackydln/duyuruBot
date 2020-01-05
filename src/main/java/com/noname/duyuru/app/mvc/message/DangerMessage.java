package com.noname.duyuru.app.mvc.message;

public class DangerMessage implements IViewMessage {
	private String message;

	public DangerMessage(String message) {
		this.message = message;
	}

	@Override
	public final String getHtmlClass() {
		return "alert-danger";
	}

	@Override
	public String getHeader() {
		return "Error!";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
