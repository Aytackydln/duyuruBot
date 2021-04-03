package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@Log4j2
public class DeleteMessage implements TelegramResponse {

	private final long chatId;
	private final long messageId;

	private boolean errorProcessed = false;

	@Override
	public String getMethod() {
		return "deleteMessage";
	}

	@Override
	@JsonIgnore
	public boolean isLimited() {
		return false;
	}

	@Override
	public TelegramResponse onError(Exception e) {
		try {
			throw e;
		} catch (HttpClientErrorException ce) {
			if (!errorProcessed) {
				errorProcessed = true;
				LOGGER.error("Error while sending delete message response: \n chatId: {} messageId: {}", chatId, messageId);
				LOGGER.error(ce.getResponseBodyAsString());
			} else {
				LOGGER.trace("Error while sending delete message response: \n chatId: {} messageId: {}", chatId, messageId);
				LOGGER.trace(ce.getResponseBodyAsString());
			}

			return null;
		} catch (Exception exception) {
		}

		LOGGER.error("Error while sending delete message response: \n chatId: {} messageId: {}", chatId, messageId);
		return null;
	}

	@Override
	public void preSend() {
		//doesn't need
	}
}
