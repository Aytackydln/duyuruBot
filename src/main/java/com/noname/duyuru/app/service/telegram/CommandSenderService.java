package com.noname.duyuru.app.service.telegram;

import com.noname.duyuru.app.configuration.TelegramClientConfig;
import com.noname.duyuru.app.json.telegram.response.SendMessage;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommandSenderService {
	private static final Logger LOGGER = LogManager.getLogger(CommandSenderService.class);

	private final RestTemplate telegramClient;
	private final CommandObserverService commandObserverService;
	private final ConfigurationSet configurationSet;

	public CommandSenderService(RestTemplate telegramClient, CommandObserverService commandObserverService, ConfigurationSet configurationSet) {
		this.telegramClient = telegramClient;
		this.commandObserverService = commandObserverService;
		this.configurationSet = configurationSet;
	}

	@Async(TelegramClientConfig.LIMITED_COMMAND_SENDER)
	void sendLimitedResponse(TelegramResponse responseToSend) {
		LOGGER.debug("sending limited telegram command");
		submitResponse(responseToSend);
	}

	@Async
	void submitResponseAsync(TelegramResponse responseToSend) {
		submitResponse(responseToSend);
	}

	private void submitResponse(TelegramResponse responseToSend) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		responseToSend.preSend();

		final HttpEntity<TelegramResponse> request = new HttpEntity<>(responseToSend, headers);
		try {
			telegramClient.postForEntity("/" + responseToSend.getMethod(), request, String.class);
		} catch (Exception e) {
			TelegramResponse errorResponse = responseToSend.onError(e);
			if (errorResponse != null)
				commandObserverService.addCommand(errorResponse);
			else {
				final SendMessage sendMessage = new SendMessage(configurationSet.getMaster().getId(),
						"unknown error while sending response: " + responseToSend.toString()
				);
				commandObserverService.addCommand(sendMessage);
			}
		}
	}
}
