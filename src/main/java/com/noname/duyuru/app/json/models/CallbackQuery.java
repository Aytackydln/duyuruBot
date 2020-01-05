package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.User;

public class CallbackQuery{
	private String id;
	private Message message;
	private String data;
	private User from;
	private String inlineMessageId;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public Message getMessage(){
		return message;
	}

	public void setMessage(Message message){
		this.message=message;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data=data;
	}

	public User getFrom(){
		return from;
	}

	public void setFrom(User from){
		this.from=from;
	}

	@JsonProperty("inline_message_id")
	public String getInlineMessageId() {
		return inlineMessageId;
	}

	@JsonProperty("inline_message_id")
	public void setInlineMessageId(String inlineMessageId) {
		this.inlineMessageId = inlineMessageId;
	}
}
