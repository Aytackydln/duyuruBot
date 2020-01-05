package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.User;

public class Update{
	private int updateId;
	private Message message;
	private CallbackQuery callbackQuery;

	@JsonProperty("update_id")
	public int getUpdateId(){
		return updateId;
	}

	@JsonProperty("update_id")
	public void setUpdateId(int updateId){
		this.updateId=updateId;
	}

	@JsonProperty("message")
	public Message getMessage(){
		return message;
	}

	@JsonProperty("message")
	public void setMessage(Message message){
		this.message=message;
	}

	@JsonProperty("callback_query")
	public CallbackQuery getCallbackQuery(){
		return callbackQuery;
	}

	@JsonProperty("callback_query")
	public void setCallbackQuery(CallbackQuery callbackQuery){
		this.callbackQuery=callbackQuery;
	}

	@JsonIgnore
	public User getUser(){
		if (message!=null){
			return message.getUser();
		}else {
			return callbackQuery.getFrom();
		}
	}
}
