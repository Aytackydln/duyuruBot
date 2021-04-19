package com.noname.duyuru.app.service.telegram;

import com.noname.duyuru.app.jpa.models.Message;
import com.noname.duyuru.app.jpa.models.Subscription;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.MessageRepository;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import com.noname.duyuru.app.jpa.repositories.TopicRepository;
import com.noname.duyuru.app.json.models.*;
import com.noname.duyuru.app.json.telegram.response.SendMessage;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.json.telegram.response.UpdateMessage;
import com.noname.duyuru.app.service.dictionary.DictionaryKeeper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommandProcessor {
	private static final String SUBSCRIBE_KEYWORD = "sub";
	private static final String UNSUBSCRIBE_KEYWORD = "unsub";

	private static final String CANCEL_STRING = "-";
	private static final String CANCEL_TEXT = "❌";

	private static final String COMMAND_SEPARATOR = "~~";

	private final SubscriptionRepository subscriptionRepository;
	private final TopicRepository topicRepository;
	private final MessageRepository messageRepository;
	private final TelegramService telegramService;
	private final DictionaryKeeper dictionaryKeeper;

	private final CustomKeyboard userKeyboard;

	public TelegramResponse processUpdate(final Update update) {
		final Message message;
		final User user;
		if (update.getMessage() == null) {//means this is a callback
			final CallbackQuery callbackQuery = update.getCallbackQuery();

			message = callbackQuery.getMessage();
			user = callbackQuery.getFrom();

			final String data = callbackQuery.getData();
			message.setText(data);
			message.setUser(user);
			try {
				messageRepository.save(message);
			} catch (DataIntegrityViolationException e) {
				return new SendMessage(user.getId(), translate(user, "REQUEST_ERROR"));
			}

			//check cancel button
			if (data.equals(CANCEL_STRING)) {
				telegramService.deleteMessage(user, message.getMessageId());
				return listSubscriptions(user);
			}

			//process callback
			final String[] commandAndParameter = data.split(COMMAND_SEPARATOR);
			final String command = commandAndParameter[0];
			final String parameter = commandAndParameter[1];
			switch (command) {
				case SUBSCRIBE_KEYWORD:
					return new UpdateMessage(message.getMessageId(), subscribe(callbackQuery, parameter));
				case UNSUBSCRIBE_KEYWORD:
					return new UpdateMessage(message.getMessageId(), unsubscribe(callbackQuery, parameter));
				default:
					LOGGER.error("unknown operation {}", command);
					return new UpdateMessage(message.getMessageId(),
							new SendMessage(user.getId(), translate(user, "REQUEST_ERROR") + "(unknown operation)"));
			}
		} else {
			message = update.getMessage();

			//TODO control timeout here

			final String text = message.getText();
			if (text == null) return null;

			messageRepository.save(message);
			telegramService.deleteMessage(message.getUser(), message.getMessageId());

			switch (text) {
				case "/list":
					return listSubscriptions(message.getUser());
				case "/subscribe":
					return listTopicsToSubscribe(message.getUser());
				case "/unsubscribe":
					return listTopicsToUnsubscribe(message.getUser());
				case "/start":
					return introduce(message.getUser());
			default:
				return null;
			}
		}
	}

	private SendMessage introduce(final User user) {
		LOGGER.info("yeni biri geldi: {}", user.getId());

		//TODO dinamik yap
		subscribe(user, "ceng");
		subscribe(user, "Mühendislik Fakültesi");

		listSubscriptions(user);

		telegramService.sendMessageToMaster("Yeni katılan: " + user.getFullName() + ", " + user.getUsername());
		final SendMessage welcome = new SendMessage(user.getId(), translate(user, "WELCOME"));
		welcome.keyboard(userKeyboard);
		return welcome;
	}

	private SendMessage listSubscriptions(final User user) {
		final List<Topic> subscriptionList = subscriptionRepository.findUserTopics(user);
		if (subscriptionList.isEmpty()) {
			return new SendMessage(user.getId(), translate(user, "SUBSCRIPTIONS_EMPTY"));
		} else {
			StringBuilder response = new StringBuilder(translate(user, "SUBSCRIBED_LIST_HEADER") + "\n");
			for (Topic t : subscriptionList) {
				response.append(t.getId()).append("\n");
			}

			SendMessage sendMessage = new SendMessage(user.getId(), response.toString());
			sendMessage.keyboard(userKeyboard);
			return sendMessage;
		}
	}

	private SendMessage listTopicsToSubscribe(final User user) {
		final List<Topic> subscriptionList = subscriptionRepository.findUserTopics(user);
		final List<Topic> allTopics = topicRepository.findAll();

		allTopics.removeAll(subscriptionList);

		final InlineKeyboard inlineKeyboard = new InlineKeyboard();
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		for (Topic topic : allTopics) {
			inlineKeyboard.addRow().add(new KeyboardItem(topic.getId(), SUBSCRIBE_KEYWORD + COMMAND_SEPARATOR + topic.getId()));
		}
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));

		return new SendMessage(user.getId(), translate(user, "SUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private SendMessage listTopicsToUnsubscribe(final User user) {
		final List<Topic> subscriptionList = subscriptionRepository.findUserTopics(user);

		final InlineKeyboard inlineKeyboard = new InlineKeyboard();
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		for (Topic topic : subscriptionList) {
			inlineKeyboard.addRow().add(new KeyboardItem(topic.getId(), UNSUBSCRIBE_KEYWORD + COMMAND_SEPARATOR + topic.getId()));
		}
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));

		return new SendMessage(user.getId(), translate(user, "UNSUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private SendMessage subscribe(final CallbackQuery query, final String topic) {
		final User user = query.getFrom();

		try {
			subscribe(user, topic);

			telegramService.sendCommand(new SendMessage(user.getId(), topic + " -> " + translate(user, "SUBSCRIBE_SUCCESS")));
			return listTopicsToSubscribe(user);
		} catch (JpaObjectRetrievalFailureException e) {
			LOGGER.error("attempted to subscribe non-existing {}", topic);
			return new SendMessage(user.getId(),
					topic + " -> " + translate(user, translate(user, "TOPIC_NOT_EXISTS")));
		} catch (Exception e) {
			LOGGER.error("Unhandled error in subscribe method", e);
			telegramService.sendMessageToMaster(user.getFullName() + " (" + user.getId() + ") " + topic + " abone olmaya çalıştı");
			return new SendMessage(user.getId(),
					topic + " -> " + translate(user, translate(user, "SUBSCRIBE_FAIL") + " (" + e.getClass() + ")"));
		}
	}

	private void subscribe(final User user, final String topic){
		final Subscription subscription = new Subscription();

		final Topic topicObj = new Topic();
		topicObj.setId(topic);

		subscription.setUser(user);
		subscription.setTopic(topicObj);

		subscriptionRepository.save(subscription);
	}

	private SendMessage unsubscribe(final CallbackQuery query, final String topic) {
		final User user = query.getFrom();

		final Subscription subscription = new Subscription();
		subscription.setUser(user);

		final Topic topicObj = new Topic();
		topicObj.setId(topic);
		subscription.setTopic(topicObj);

		try {
			subscriptionRepository.delete(subscription);

			telegramService.sendCommand(new SendMessage(user.getId(), topic + " -> " + translate(user, "UNSUBSCRIBE_SUCCESS")));
			return listTopicsToUnsubscribe(user);
		} catch (Exception e) {
			LOGGER.error("Unhandled error in unsubscribe method", e);
			return new SendMessage(user.getId(), topic + " -> " + translate(user, "UNSUBSCRIBE_FAIL"));
		}
	}

	/**
	 * @deprecated TranslatorService oluşturup oradan çağır
	 */
	@Deprecated
	private String translate(final User user, final String sentence) {
		return dictionaryKeeper.getTranslation(user.getLanguage(), sentence);
	}

}
