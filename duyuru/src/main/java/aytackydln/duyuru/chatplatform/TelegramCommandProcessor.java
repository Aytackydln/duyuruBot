package aytackydln.duyuru.chatplatform;

import aytackydln.chattools.telegram.dto.models.*;
import aytackydln.chattools.telegram.dto.response.SendMessage;
import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import aytackydln.chattools.telegram.dto.response.UpdateMessage;
import aytackydln.duyuru.chatplatform.port.TelegramPort;
import aytackydln.duyuru.common.TranslationPort;
import aytackydln.duyuru.common.semantic.DomainComponent;
import aytackydln.duyuru.configuration.ConfigurationSet;
import aytackydln.duyuru.mapper.TelegramMapper;
import aytackydln.duyuru.message.ChatMessage;
import aytackydln.duyuru.message.port.ChatMessagePort;
import aytackydln.duyuru.subscriber.Subscriber;
import aytackydln.duyuru.subscriber.SubscriberFacade;
import aytackydln.duyuru.subscriber.Subscription;
import aytackydln.duyuru.subscriber.port.SubscriptionPort;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.topic.port.TopicPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DomainComponent
public class TelegramCommandProcessor {
	private static final String SUBSCRIBE_KEYWORD = "sub";
	private static final String UNSUBSCRIBE_KEYWORD = "unsub";

	private static final String CANCEL_STRING = "-";
	private static final String CANCEL_TEXT = "❌";

	private static final String COMMAND_SEPARATOR = "~~";

	//TODO too much dependencies :(
	private final SubscriberFacade subscriberFacade;
	private final SubscriptionPort subscriptionPort;
	private final TopicPort topicPort;
	private final ChatMessagePort chatMessagePort;
	private final TelegramPort telegramPort;
	private final TelegramFacade telegramFacade;
	private final ConfigurationSet configurationSet;
	private final TranslationPort translationPort;

	private final CustomKeyboard userKeyboard;

	private final TelegramMapper telegramMapper;

	public TelegramResponse processUpdate(final Update update) {
		if (update.getMyChatMember() != null) {
			processMemberChange(update);
			return null;
		} else if (update.getMessage() != null) {
			return processMessage(update);
		} else if (update.getCallbackQuery() != null) {
			return processCallback(update);
		}

		throw new UnsupportedOperationException("Unknown update :" + update);
	}

	private void processMemberChange(Update update) {
		var chatDetails = update.getMyChatMember();
		var chatMember = chatDetails.getNewChatMember();
		String newMemberStatus = chatMember.getStatus();
		long newMemberId = chatMember.getUser().getId();

		if (configurationSet.getBotId() == newMemberId) {
			switch (newMemberStatus) {
				case "member" -> userUnblockedBot(update);
				case "kicked" -> userBlockedBot(update);
				default -> throw new IllegalStateException("Unknown member state");
			}
		}
	}

	private void userBlockedBot(Update update) {
		var user = update.getUser();
		subscriberFacade.setUserStatusDisabled(user.getId());
	}

	private void userUnblockedBot(Update update) {
		var user = update.getUser();
		subscriberFacade.clearUserStatus(user.getId());
	}

	private SendMessage processMessage(Update update) {
		var message = telegramMapper.toMessage(update.getMessage());

		//TODO control timeout (if the message is too old)

		final String text = message.getText();
		if (text == null) return null;

		chatMessagePort.update(message);
		telegramPort.deleteMessage(message.getUser(), message.getMessageId());

		return switch (text) {
			case "/list" -> listSubscriptions(message.getUser());
			case "/subscribe" -> listTopicsToSubscribe(message.getUser());
			case "/unsubscribe" -> listTopicsToUnsubscribe(message.getUser());
			case "/start" -> introduce(message.getUser());
			default -> null;
		};
	}

	private TelegramResponse processCallback(Update update) {
		ChatMessage message = telegramMapper.toMessageFromCallback(update);
		var user = message.getUser();
		var data = update.getCallbackQuery().getData();

		chatMessagePort.update(message);

		//check cancel button
		if (data.equals(CANCEL_STRING)) {
			telegramPort.deleteMessage(user, message.getMessageId());
			return listSubscriptions(user);
		}

		//process callback
		final String[] commandAndParameter = data.split(COMMAND_SEPARATOR);
		final String command = commandAndParameter[0];
		final String parameter = commandAndParameter[1];
		switch (command) {
			case SUBSCRIBE_KEYWORD:
				return new UpdateMessage(message.getMessageId(), subscribe(user, parameter));
			case UNSUBSCRIBE_KEYWORD:
				return new UpdateMessage(message.getMessageId(), unsubscribe(user, parameter));
			default:
				LOGGER.error("unknown operation {}", command);
				return new UpdateMessage(message.getMessageId(),
						new SendMessage(user.getId(), translate(user, "REQUEST_ERROR") + "(unknown operation)"));
		}
	}

	private SendMessage introduce(Subscriber chatUser) {
		LOGGER.info("yeni biri geldi: {}", chatUser.getId());

		//TODO dinamik yap
		subscribe(chatUser, "ceng");
		subscribe(chatUser, "Mühendislik Fakültesi");

		listSubscriptions(chatUser);

		telegramFacade.sendMessageToMaster("Yeni katılan: " + chatUser.getFullName() + ", " + chatUser.getUsername());
		var welcome = new SendMessage(chatUser.getId(), translate(chatUser, "WELCOME"));
		welcome.keyboard(userKeyboard);
		return welcome;
	}

	private SendMessage listSubscriptions(Subscriber subscriber) {
		var subscriptionList = subscriberFacade.findUserTopics(subscriber);
		if (subscriptionList.isEmpty()) {
			return new SendMessage(subscriber.getId(), translate(subscriber, "SUBSCRIPTIONS_EMPTY"));
		} else {
			var response = new StringBuilder(translate(subscriber, "SUBSCRIBED_LIST_HEADER") + "\n");
			for (Topic t : subscriptionList) {
				response.append(t.getId()).append("\n");
			}

			var sendMessage = new SendMessage(subscriber.getId(), response.toString());
			sendMessage.keyboard(userKeyboard);
			return sendMessage;
		}
	}

	private SendMessage listTopicsToSubscribe(final Subscriber subscriber) {
		var subscriptionList = subscriberFacade.findUserTopics(subscriber);
		var allTopics = topicPort.retrieveAll();

		allTopics.removeAll(subscriptionList);

		var inlineKeyboard = createTopicKeyboard(allTopics, SUBSCRIBE_KEYWORD);

		return new SendMessage(subscriber.getId(), translate(subscriber, "SUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private Keyboard createTopicKeyboard(List<Topic> allTopicEntities, String subscribeKeyword) {
		var inlineKeyboard = new InlineKeyboard();
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		for (Topic topic : allTopicEntities) {
			inlineKeyboard.addRow().add(new KeyboardItem(topic.getId(), subscribeKeyword + COMMAND_SEPARATOR + topic.getId()));
		}
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		return inlineKeyboard;
	}

	private SendMessage listTopicsToUnsubscribe(Subscriber subscriber) {
		var subscriptionList = subscriberFacade.findUserTopics(subscriber);

		var inlineKeyboard = createTopicKeyboard(subscriptionList, UNSUBSCRIBE_KEYWORD);

		return new SendMessage(subscriber.getId(), translate(subscriber, "UNSUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private SendMessage subscribe(Subscriber subscriber, final String topic) {
		try {
			saveSubscription(subscriber, topic);

			telegramPort.sendCommand(new SendMessage(subscriber.getId(), topic + " -> " + translate(subscriber, "SUBSCRIBE_SUCCESS")));
			return listTopicsToSubscribe(subscriber);
		} catch (Exception e) {
			LOGGER.error("Unhandled error in subscribe method", e);
			telegramFacade.sendMessageToMaster(subscriber.getFullName() + " (" + subscriber.getId() + ") " + topic + " abone olmaya çalıştı");
			return new SendMessage(subscriber.getId(),
					topic + " -> " + translate(subscriber, translate(subscriber, "SUBSCRIBE_FAIL") + " (" + e.getClass() + ")"));
		}
	}

	private void saveSubscription(Subscriber subscriber, final String topic) {
		final var subscription = new Subscription();

		final var topicObj = new Topic();
		topicObj.setId(topic);

		subscription.setUser(subscriber);
		subscription.setTopic(topicObj);

		subscriptionPort.update(subscription);
	}

	private SendMessage unsubscribe(Subscriber subscriber, final String topic) {
		var subscription = new Subscription();
		subscription.setUser(subscriber);

		var topicObj = new Topic();
		topicObj.setId(topic);
		subscription.setTopic(topicObj);

		try {
			subscriptionPort.remove(subscription);

			telegramPort.sendCommand(new SendMessage(subscriber.getId(), topic + " -> " + translate(subscriber, "UNSUBSCRIBE_SUCCESS")));
			return listTopicsToUnsubscribe(subscriber);
		} catch (Exception e) {
			LOGGER.error("Unhandled error in unsubscribe method", e);
			return new SendMessage(subscriber.getId(), topic + " -> " + translate(subscriber, "UNSUBSCRIBE_FAIL"));
		}
	}

	/**
	 * @deprecated TranslatorService oluşturup oradan çağır
	 */
	@Deprecated
	private String translate(final Subscriber subscriber, final String sentence) {
		return translationPort.translate(sentence, subscriber.getLanguage());
	}

}
