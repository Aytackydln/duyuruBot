package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.json.models.Keyboard;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@ToString(onlyExplicitlyIncluded = true)
@Log4j2
public class SendMessage implements TelegramResponse {

	@ToString.Include
	private final long chatId;

	@ToString.Include
	private String text;
	private boolean disableNotification;
	private Keyboard replyMarkup;

	private boolean errorProcessed = false;

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

	public String getMethod() {
		return "sendMessage";
	}

	@JsonProperty
	public String getParseMode() {
		return "HTML";
	}

	@Override
	@JsonIgnore
	public boolean isLimited() {
		return true;
	}

	@Override
	public TelegramResponse onError(Exception e) {
		try {
			throw e;
		} catch (HttpClientErrorException ce) {
			switch (ce.getRawStatusCode()) {
				case 403:
					LOGGER.info("User {} is disabled. Subscriptions should be deleted.\n{}",
							chatId, ce.getResponseBodyAsString());
					//TODO sublarını sil
					return null;
				default:
					if (!errorProcessed) {
						errorProcessed = true;
						return this;
					}
					return null;
			}
		} catch (ResourceAccessException rae) {
			LOGGER.error("Error while handling", rae);
			try {
				Thread.sleep(10000);    //TODO handle infinite loop?
			} catch (InterruptedException ignored) {
			}
			return this;
		} catch (Exception exception) {
			LOGGER.error("unknown error", exception);
			if (!errorProcessed) {
				errorProcessed = true;
				return this;
			}
			return null;
		}
	}

	@Override
	public void preSend() {
		//dont make notification sound between 00:00-07:00
		final Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(new Date());   // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 7) {
			silent();
		}
	}
}
