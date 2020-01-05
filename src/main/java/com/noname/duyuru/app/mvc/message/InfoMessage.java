package com.noname.duyuru.app.mvc.message;

public class InfoMessage implements IViewMessage {
	private String message;

	InfoMessage(String message) {
		this.message = message;
	}

	@Override
	public final String getHtmlClass() {
		return "alert-info";
	}

	@Override
	public String getHeader() {
		return "Info!";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
