package com.noname.duyuru.app.service.telegram;

import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.json.models.Update;
import com.noname.duyuru.app.json.models.Updates;
import com.noname.duyuru.app.mvc.message.DangerMessage;
import com.noname.duyuru.app.mvc.message.IViewMessage;
import com.noname.duyuru.app.mvc.message.SuccessMessage;
import com.noname.duyuru.app.service.dictionary.DictionaryKeeper;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class PollingService implements DisposableBean {
	private static final Logger LOGGER = LogManager.getLogger(PollingService.class);

	private final TelegramService telegramService;
	private final ConfigurationSet configurationSet;
	private final DictionaryKeeper dictionaryKeeper;
	private final CommandProcessor commandProcessor;
	private final RestTemplate telegramClient;
	private final Random random = new Random();
	private int lastUpdate = 0;
	private Thread pollingThread;

	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				boolean noConnection = true;
				while (true) {
					checkInterrupt();
					try {
						if (noConnection) {
							LOGGER.debug("sending apology messages");
							apologyInvalidMessages();
							noConnection = false;
							LOGGER.info("apology messages sent");
							if (!configurationSet.getWebhookUrl().equals("") && configurationSet.isWebHookEnabled()) {
								createWebhook();
								return;
							} else
								checkInterrupt();
						}
						for (final Update update : getUpdates()) {
							try {
								lastUpdate = update.getUpdateId() + 1;
								telegramService.sendCommand(commandProcessor.processUpdate(update));
							} catch (Exception e) {
								LOGGER.error(e);
							}
						}
					} catch (final ResourceAccessException ignored) {
						if (noConnection)
							LOGGER.debug("still no connection");
						else {
							LOGGER.error("lost connection to telegram api");
							noConnection = true;
						}
					} catch (final HttpClientErrorException e) {
						if (e.getRawStatusCode() == 409) {
							if (configurationSet.getWebhookToken().equals("")) {
								LOGGER.error("webhook exist while polling is enabled. Disabling webhook");
								deleteWebhook();
							} else {
								LOGGER.info("webhook is enabled. Stopping poller thread");
								return;
							}
						} else {
							LOGGER.error("Poller unhandled error code " + e.getRawStatusCode(), e);
						}
					} catch (final RestClientException e) {
						LOGGER.error(e);
						noConnection = true;
					} catch (Exception e) {
						LOGGER.error(e);
						LOGGER.error("could not send apology messages");
					}
					checkInterrupt();
				}
			} catch (InterruptedException ignored) {
				LOGGER.info("polling thread stopped");
			}
		}

		private void checkInterrupt() throws InterruptedException {
			if (Thread.currentThread().isInterrupted()) {
				LOGGER.info("stopping polling thread");
				throw new InterruptedException();
			}
			Thread.sleep(1500);
		}
	};

	public PollingService(TelegramService telegramService, ConfigurationSet configurationSet, DictionaryKeeper dictionaryKeeper,
						  CommandProcessor commandProcessor, RestTemplate telegramClient) {
		this.telegramService = telegramService;
		this.configurationSet = configurationSet;
		this.dictionaryKeeper = dictionaryKeeper;
		this.commandProcessor = commandProcessor;
		this.telegramClient = telegramClient;
	}

	public void startPolling() {
		pollingThread = new Thread(runnable, "poller");
		pollingThread.start();
	}

	public void apologyInvalidMessages() {
		final Set<User> apologyList = new HashSet<>();
		final Updates invalidUpdates = getUpdates();
		for (final Update invalidUpdate : invalidUpdates) {
			final User user = new User(); //TODO refactor
			final Message message = invalidUpdate.getMessage();
			if (message != null)
				user.setId(message.chat.getId());
			else continue;
			apologyList.add(user);
			lastUpdate = invalidUpdate.getUpdateId();
		}
		lastUpdate++;

		for (final User user : apologyList) {
			telegramService.sendMessage(user,
					dictionaryKeeper.getTranslation(user.getLanguage(), "UNAVAILABLE_APOLOGY"));
		}
	}

	public String deleteWebhook() throws InterruptedException {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);

		try {
			ResponseEntity<String> response = telegramClient.postForEntity("/deleteWebhook", request, String.class);

			configurationSet.setWebhookToken("");
			configurationSet.setWebhookEnabled(false);
			if (pollingThread!=null&&!pollingThread.isAlive()) {
				startPolling();
			}

			return response.toString();
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 429) {
				LOGGER.info("There is 429 on deleteWebhook. Waiting 10 seconds");
				Thread.sleep(10000);
				return deleteWebhook();
			}
			LOGGER.error("Unhandled error code " + e.getRawStatusCode(), e);
			return e.getMessage();
		}
	}

	public IViewMessage createWebhook() {
		final String randomChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		final StringBuilder randomString = new StringBuilder();
		final int TOKEN_LENGTH = 12;
		for (int i = 0; i < TOKEN_LENGTH; i++) {
			randomString.append(randomChars.charAt(random.nextInt(randomChars.length())));
		}
		String url = configurationSet.getWebhookUrl() + "webhook/" + randomString.toString();

		LOGGER.info("setting webhook on: " + url);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("url", url);
		//map.add("certificate", certificate); //TODO certificate için file tipinde veri tut
		final String certificate = configurationSet.getCertificate();
		if (!certificate.equals("")) {
			map.add("certificate", configurationSet.getCertificate().getBytes());
		}

		final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
		try {
			ResponseEntity<String> response = telegramClient.postForEntity("/setWebhook", request, String.class);

			configurationSet.setWebhookToken(randomString.toString());
			configurationSet.setWebhookEnabled(true);
			if (pollingThread!=null)pollingThread.interrupt();
			return new SuccessMessage(response.toString());
		} catch (HttpClientErrorException e) {
			return new DangerMessage(e.getMessage());
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Destroying poller");
		if (pollingThread.isAlive()) {
			LOGGER.info("Polling thread is still up");
			if (!pollingThread.isInterrupted()) {
				LOGGER.info("Interrupting polling thread");
				pollingThread.interrupt();
				try {
					pollingThread.join();
					LOGGER.info("Polling thread closed.");
				} catch (InterruptedException e) {
					LOGGER.info(e);
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private Updates getUpdates() {
		return telegramClient.getForObject("/getUpdates?offset=" + lastUpdate, Updates.class);
	}
}
