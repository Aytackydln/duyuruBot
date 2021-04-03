package com.noname.duyuru.app.mvc.message;

import lombok.Value;

@Value
public class WarningMessage implements IViewMessage {
	String message;

	@Override
	public final String getHtmlClass() {
		return "alert-warning";
	}

	@Override
	public String getHeader() {
		return "Warning!";
	}
}
