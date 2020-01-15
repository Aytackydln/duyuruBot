package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.json.response.DeleteMessage;
import com.noname.duyuru.app.json.response.JsonResponseEntity;
import com.noname.duyuru.app.json.response.SendMessage;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class MessageSender implements DisposableBean {
	private static final Logger LOGGER = LogManager.getLogger(MessageSender.class);

	private final RestTemplate telegramClient;

	private final ConfigurationSet configurationSet;

	private final BlockingQueue<JsonResponseEntity> messageQueue = new ArrayBlockingQueue<>(256);
	private final Thread sendingThread = new Thread() {
		@Override
		public void run() {
			try {
				while (true) {
					if (Thread.interrupted()) {
						LOGGER.info("stopping message thread");
						return;
					}
					final JsonResponseEntity responseToSend = messageQueue.take();
					final long startTime = System.currentTimeMillis();

					final HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);

					if (responseToSend instanceof SendMessage) {//dont make notification sound between 00:00-07:00
						final Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
						calendar.setTime(new Date());   // assigns calendar to given date
						final int hour = calendar.get(Calendar.HOUR_OF_DAY);
						if (hour < 7) {
							((SendMessage) responseToSend).silent();
						}
					}

					final HttpEntity<JsonResponseEntity> request = new HttpEntity<>(responseToSend, headers);
					try {
						telegramClient.postForEntity("/" + responseToSend.getMethod(), request, String.class);

					} catch (final HttpClientErrorException e) {
						//TODO 403'te subları sil
						JsonResponseEntity failedResponse = responseToSend.onError(e);
						if (failedResponse != null)
							messageQueue.add(failedResponse);
					} catch (final ResourceAccessException e) {
						messageQueue.add(responseToSend);
						Thread.sleep(10000);
					} catch (Exception e) {
						LOGGER.error("Unhandled exception", e);
						sendMessageToMaster("unknown error while sending response: " + request.toString());
					}

					final long timeElapsed = System.currentTimeMillis() - startTime;
					if (Thread.interrupted()) {
						LOGGER.info("stopping message thread");
						return;
					}
					Thread.sleep(Math.max(((int) (34 - timeElapsed)), 0));
				}
			} catch (InterruptedException ignored) {
				LOGGER.info("Stopping MessageSender thread");
			}catch (IllegalStateException e){
				messageQueue.clear();
				sendMessageToMaster("Sending thread overloaded");
				//TODO bu threadde oluşanlar için ayrı queue?
			}
		}
	};

	public MessageSender(final ConfigurationSet configurationSet, RestTemplate telegramClient) {
		this.configurationSet = configurationSet;
		this.telegramClient = telegramClient;
		sendingThread.start();

		sendMessageToMaster("I have woken up, master");
	}

	public void destroy() {
		sendMessageToMaster("Goodbye, master...");
		sendingThread.interrupt();
	}

	void sendMessage(final User user, final String message) {
		final SendMessage sendMessage = new SendMessage(user.getId(), message);
		try {
			messageQueue.put(sendMessage);
		} catch (InterruptedException ignored) {
		}
	}

	void sendMessageToMaster(final String message) {
		sendMessage(configurationSet.getMaster(), message);
	}

	void deleteMessage(final User user, final long messageId) {
		submitResponseAsync(new DeleteMessage(user.getId(), messageId));
	}

	void send(final JsonResponseEntity response) {
		if (response.isLimited()) {
			try {
				messageQueue.put(response);    //TODO @Async("msgSender") li method yap
			} catch (InterruptedException ignored) {
			}
		} else {
			submitResponseAsync(response);
		}
	}

	@Async
	void submitResponseAsync(JsonResponseEntity responseEntity) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<JsonResponseEntity> request = new HttpEntity<>(responseEntity, headers);

		try {
			telegramClient.postForEntity("/" + responseEntity.getMethod(), request, String.class);
		} catch (HttpClientErrorException e) {
			//TODO delete için log tutma?
			LOGGER.error("error with sending command", e);
		}
	}
}
