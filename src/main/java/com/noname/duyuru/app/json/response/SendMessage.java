package com.noname.duyuru.app.json.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Keyboard;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessage implements JsonResponseEntity {
	private static final Logger LOGGER = LogManager.getLogger(SendMessage.class);

	private long chatId;
	private String text;
	private boolean disableNotification;
	private Keyboard replyMarkup;

	public SendMessage(final long chatId, final String text) {
		this.chatId = chatId;
		this.text = text;
	}

	public final SendMessage keyboard(final Keyboard keyboard) {
		replyMarkup = keyboard;
		return this;
	}

	public final SendMessage silent() {
		disableNotification = true;
		return this;
	}

	public final String getMethod() {
		return "sendMessage";
	}

	@JsonProperty("parse_mode")
	public String getParseMode() {
		return "HTML";
	}

	@JsonProperty("chat_id")
	public long getChatId() {
		return chatId;
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	@JsonProperty("disable_notification")
	public boolean isDisableNotification() {
		return disableNotification;
	}

	@JsonProperty("reply_markup")
	public Keyboard getReplyMarkup() {
		return replyMarkup;
	}

	@JsonProperty("reply_markup")
	public void setReplyMarkup(Keyboard replyMarkup) {
		this.replyMarkup = replyMarkup;
	}

	@Override
	@JsonIgnore
	public boolean isLimited() {
		return true;
	}

	@Override
	public JsonResponseEntity onError(HttpClientErrorException e) {
		switch (e.getRawStatusCode()) {
		case 403:
			LOGGER.info("User " + chatId + " is disabled. Subscriptions should be deleted.\n"+e.getResponseBodyAsString());
			//TODO sublarını sil
			return null;
		default:
			return this;
		}
	}
}
