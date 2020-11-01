package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface TelegramResponse {
	String getMethod();

	@JsonIgnore
	boolean isLimited();

	TelegramResponse onError(Exception e);

	void preSend();
}
