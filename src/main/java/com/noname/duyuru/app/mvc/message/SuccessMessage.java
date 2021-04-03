package com.noname.duyuru.app.mvc.message;

import lombok.Value;

@Value
public class SuccessMessage implements IViewMessage {
	String message;

	@Override
	public final String getHtmlClass() {
		return "alert-success";
	}

	@Override
	public String getHeader() {
		return "Success!";
	}
}
