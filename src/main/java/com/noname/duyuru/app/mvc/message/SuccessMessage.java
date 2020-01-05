package com.noname.duyuru.app.mvc.message;

public class SuccessMessage implements IViewMessage {
	private final String message;

	public SuccessMessage(String message) {
		this.message = message;
	}

	@Override
	public final String getHtmlClass() {
		return "alert-success";
	}

	@Override
	public String getHeader() {
		return "Success!";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
