package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.client.HttpClientErrorException;

public interface TelegramResponse {
	String getMethod();

	@JsonIgnore
	boolean isLimited();

	TelegramResponse onError(HttpClientErrorException e);
}
