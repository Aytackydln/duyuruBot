package aytackydln.duyuru.adapter.telegram;

import aytackydln.chattools.telegram.TelegramMessage;
import aytackydln.chattools.telegram.dto.models.Update;
import aytackydln.chattools.telegram.dto.models.Updates;
import aytackydln.duyuru.chatplatform.TelegramCommandProcessor;
import aytackydln.duyuru.chatplatform.port.TelegramPort;
import aytackydln.duyuru.configuration.ConfigurationSet;
import aytackydln.duyuru.mvc.message.DangerMessage;
import aytackydln.duyuru.mvc.message.SuccessMessage;
import aytackydln.duyuru.mvc.message.ViewMessage;
import aytackydln.duyuru.subscriber.Subscriber;
import aytackydln.duyuru.dictionary.DictionaryKeeper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequiredArgsConstructor
@Log4j2
public class PollingService implements DisposableBean {

	//TODO too much dependency, too much code :(

	private final TelegramPort telegramService;
	private final ConfigurationSet configurationSet;
	private final DictionaryKeeper dictionaryKeeper;
	private final TelegramCommandProcessor telegramCommandProcessor;
	private final RestTemplate telegramClient;
	private final Random random = new Random();
	private int lastUpdate = 0;
	private Thread pollingThread;

	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				var noConnection = true;
				while (true) {
					checkInterrupt();
					try {
						if (noConnection) {
							LOGGER.debug("sending apology messages");
							apologyInvalidMessages();
							noConnection = false;
							LOGGER.info("apology messages sent");
							if (!configurationSet.getWebhookUrl().equals("") && configurationSet.isWebhookEnabled()) {
								createWebhook();
								return;
							} else
								checkInterrupt();
						}
						for (final Update update : getUpdates()) {
							try {
								lastUpdate = update.getUpdateId() + 1;
								telegramService.sendCommand(telegramCommandProcessor.processUpdate(update));
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

	public void startPolling() {
		pollingThread = new Thread(runnable, "poller");
		pollingThread.start();
	}

	public void apologyInvalidMessages() {
		final Set<Subscriber> apologyList = new HashSet<>();
		final Updates invalidUpdates = getUpdates();
		for (final Update invalidUpdate : invalidUpdates) {
			final Subscriber subscriber = new Subscriber(); //TODO refactor
			final TelegramMessage message = invalidUpdate.getMessage();
			if (message != null)
				subscriber.setId(message.getChat().getPlatformChatId());
			else continue;
			apologyList.add(subscriber);
			lastUpdate = invalidUpdate.getUpdateId();
		}
		lastUpdate++;

		for (final Subscriber subscriber : apologyList) {
			telegramService.sendMessage(subscriber,
					dictionaryKeeper.getTranslation(subscriber.getLanguage(), "UNAVAILABLE_APOLOGY"));
		}
	}

	public String deleteWebhook() throws InterruptedException {
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);

		try {
			ResponseEntity<String> response = telegramClient.postForEntity("/deleteWebhook", request, String.class);

			configurationSet.setWebhookToken("");
			configurationSet.setWebhookEnabled(false);
			if (pollingThread != null && !pollingThread.isAlive()) {
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

	public ViewMessage createWebhook() {
		var randomChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		var randomString = new StringBuilder();
		var tokenLength = 12;
		for (var i = 0; i < tokenLength; i++) {
			randomString.append(randomChars.charAt(random.nextInt(randomChars.length())));
		}
		String url = configurationSet.getWebhookUrl() + "webhook/" + randomString.toString();

		LOGGER.info("setting webhook on: " + url);
		var headers = new HttpHeaders();
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
