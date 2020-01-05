package com.noname.duyuru.app.json.response;

import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JsonResponseEntity{
	String getMethod();
	@JsonIgnore
	boolean isLimited();

	JsonResponseEntity onError(HttpClientErrorException e);
}
