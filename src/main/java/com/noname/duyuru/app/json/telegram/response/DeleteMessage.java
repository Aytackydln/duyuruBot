package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;

public class DeleteMessage implements TelegramResponse {
	private static final Logger LOGGER = LogManager.getLogger(DeleteMessage.class);

	private final long chatId;
	private final long messageId;

	@JsonIgnore
	private boolean errorProcessed = false;

	public DeleteMessage(final long chat_id, final long message_id) {
		this.chatId = chat_id;
		this.messageId = message_id;
	}

	@Override
	public String getMethod() {
		return "deleteMessage";
	}

	@JsonProperty("chat_id")
	public long getChatId() {
		return chatId;
	}

	@JsonProperty("message_id")
	public long getMessageId() {
		return messageId;
	}

	@Override
	@JsonIgnore
	public boolean isLimited() {
		return false;
	}

	@Override
	public TelegramResponse onError(HttpClientErrorException e) {
		if (!errorProcessed) {
			LOGGER.error("Error while sending delete message response: \n chatId: {} messageId: {}", chatId, messageId);
			LOGGER.error(e.getResponseBodyAsString());
		} else {
			LOGGER.trace("Error while sending delete message response: \n chatId: {} messageId: {}", chatId, messageId);
			LOGGER.trace(e.getResponseBodyAsString());
		}

		return null;
	}
}
