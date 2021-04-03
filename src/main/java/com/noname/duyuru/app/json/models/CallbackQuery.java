package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.User;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class CallbackQuery{
	private String id;
	private Message message;
	private String data;
	private User from;
	private String inlineMessageId;
}
