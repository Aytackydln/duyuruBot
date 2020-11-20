package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Keyboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessage implements TelegramResponse {
	private static final Logger LOGGER = LogManager.getLogger(SendMessage.class);

	private final long chatId;
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
			LOGGER.error(rae);
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
		final Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(new Date());   // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 7) {
			silent();
		}
	}

	@Override
	public String toString() {
		return "SendMessage{" +
				"chatId=" + chatId +
				", text='" + text + '\'' +
				'}';
	}
}
