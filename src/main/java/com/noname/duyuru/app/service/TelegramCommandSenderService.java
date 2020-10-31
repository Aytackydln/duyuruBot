package com.noname.duyuru.app.service;

import com.noname.duyuru.app.configuration.TelegramClientConfig;
import com.noname.duyuru.app.json.telegram.response.SendMessage;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class TelegramCommandSenderService {
	private static final Logger LOGGER = LogManager.getLogger(TelegramCommandSenderService.class);

	private final RestTemplate telegramClient;

	public TelegramCommandSenderService(RestTemplate telegramClient) {
		this.telegramClient = telegramClient;
	}

	@Async(TelegramClientConfig.LIMITED_COMMAND_SENDER)
	void sendLimitedResponse(TelegramResponse responseToSend) throws Exception {
		LOGGER.debug("sending limited telegram command");
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		//TODO Liskov's principle? responseToSend.preSend() ?
		if (responseToSend instanceof SendMessage) {//dont make notification sound between 00:00-07:00
			final Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
			calendar.setTime(new Date());   // assigns calendar to given date
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 7) {
				((SendMessage) responseToSend).silent();
			}
		}

		final HttpEntity<TelegramResponse> request = new HttpEntity<>(responseToSend, headers);
		telegramClient.postForEntity("/" + responseToSend.getMethod(), request, String.class);
	}

	@Async
	void submitResponseAsync(TelegramResponse responseEntity) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<TelegramResponse> request = new HttpEntity<>(responseEntity, headers);

		telegramClient.postForEntity("/" + responseEntity.getMethod(), request, String.class);
	}
}
