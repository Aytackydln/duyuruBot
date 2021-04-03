package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.User;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class Update{
	private int updateId;
	private Message message;
	private CallbackQuery callbackQuery;

	@JsonIgnore
	public User getUser(){
		if (message!=null){
			return message.getUser();
		}else {
			return callbackQuery.getFrom();
		}
	}
}
