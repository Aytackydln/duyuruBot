package com.noname.duyuru.app.mvc.message;

import lombok.Value;

@Value
public class DangerMessage implements ViewMessage {
	String message;

	@Override
	public final String getHtmlClass() {
		return "alert-danger";
	}

	@Override
	public String getHeader() {
		return "Error!";
	}
}
