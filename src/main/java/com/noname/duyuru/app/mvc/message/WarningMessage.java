package com.noname.duyuru.app.mvc.message;

public class WarningMessage implements IViewMessage {
	private final String message;

	public WarningMessage(String message){
		this.message=message;
	}

	@Override
	public final String getHtmlClass() {
		return "alert-warning";
	}

	@Override
	public String getHeader() {
		return "Warning!";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
