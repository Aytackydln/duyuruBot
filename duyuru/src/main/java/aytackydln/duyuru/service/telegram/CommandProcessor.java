package aytackydln.duyuru.service.telegram;

import aytackydln.chattools.telegram.dto.models.*;
import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.models.MessageEntity;
import aytackydln.duyuru.jpa.models.SubscriptionEntity;
import aytackydln.duyuru.jpa.models.TopicEntity;
import aytackydln.duyuru.mapper.TelegramMapper;
import aytackydln.duyuru.service.ChatUserService;
import aytackydln.duyuru.service.dictionary.DictionaryKeeper;
import aytackydln.duyuru.setting.ConfigurationSet;
import aytackydln.duyuru.jpa.repository.MessageRepository;
import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
import aytackydln.duyuru.jpa.repository.TopicRepository;
import aytackydln.chattools.telegram.dto.response.SendMessage;
import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import aytackydln.chattools.telegram.dto.response.UpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.Nullable;
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
	private final ChatUserService chatUserService;
	private final DictionaryKeeper dictionaryKeeper;
	private final ConfigurationSet configurationSet;

	private final CustomKeyboard userKeyboard;

	private final TelegramMapper telegramMapper;

	@Nullable
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

	@Nullable
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
		chatUserService.setUserStatusDisabled(user.getId());
	}

	private void userUnblockedBot(Update update) {
		var user = update.getUser();
		chatUserService.clearUserStatus(user.getId());
	}

	private SendMessage processMessage(Update update) {
		var message = telegramMapper.toMessage(update.getMessage());

		//TODO control timeout (if the message is too old)

		final String text = message.getText();
		if (text == null) return null;

		messageRepository.save(message);
		telegramService.deleteMessage(message.getUser(), message.getMessageId());

		return switch (text) {
			case "/list" -> listSubscriptions(message.getUser());
			case "/subscribe" -> listTopicsToSubscribe(message.getUser());
			case "/unsubscribe" -> listTopicsToUnsubscribe(message.getUser());
			case "/start" -> introduce(message.getUser());
			default -> null;
		};
	}

	private TelegramResponse processCallback(Update update) {
		MessageEntity message = telegramMapper.toMessageFromCallback(update);
		var user = message.getUser();
		var data = update.getCallbackQuery().getData();

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
				return new UpdateMessage(message.getMessageId(), subscribe(user, parameter));
			case UNSUBSCRIBE_KEYWORD:
				return new UpdateMessage(message.getMessageId(), unsubscribe(user, parameter));
			default:
				LOGGER.error("unknown operation {}", command);
				return new UpdateMessage(message.getMessageId(),
						new SendMessage(user.getId(), translate(user, "REQUEST_ERROR") + "(unknown operation)"));
		}
	}

	private SendMessage introduce(UserEntity chatUser) {
		LOGGER.info("yeni biri geldi: {}", chatUser.getId());

		//TODO dinamik yap
		subscribe(chatUser, "ceng");
		subscribe(chatUser, "Mühendislik Fakültesi");

		listSubscriptions(chatUser);

		telegramService.sendMessageToMaster("Yeni katılan: " + chatUser.getFullName() + ", " + chatUser.getUsername());
		var welcome = new SendMessage(chatUser.getId(), translate(chatUser, "WELCOME"));
		welcome.keyboard(userKeyboard);
		return welcome;
	}

	private SendMessage listSubscriptions(final UserEntity userEntity) {
		var subscriptionList = subscriptionRepository.findUserTopics(userEntity);
		if (subscriptionList.isEmpty()) {
			return new SendMessage(userEntity.getId(), translate(userEntity, "SUBSCRIPTIONS_EMPTY"));
		} else {
			var response = new StringBuilder(translate(userEntity, "SUBSCRIBED_LIST_HEADER") + "\n");
			for (TopicEntity t : subscriptionList) {
				response.append(t.getId()).append("\n");
			}

			var sendMessage = new SendMessage(userEntity.getId(), response.toString());
			sendMessage.keyboard(userKeyboard);
			return sendMessage;
		}
	}

	private SendMessage listTopicsToSubscribe(final UserEntity userEntity) {
		var subscriptionList = subscriptionRepository.findUserTopics(userEntity);
		var allTopics = topicRepository.findAll();

		allTopics.removeAll(subscriptionList);

		var inlineKeyboard = createTopicKeyboard(allTopics, SUBSCRIBE_KEYWORD);

		return new SendMessage(userEntity.getId(), translate(userEntity, "SUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private Keyboard createTopicKeyboard(List<TopicEntity> allTopicEntities, String subscribeKeyword) {
		var inlineKeyboard = new InlineKeyboard();
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		for (TopicEntity topicEntity : allTopicEntities) {
			inlineKeyboard.addRow().add(new KeyboardItem(topicEntity.getId(), subscribeKeyword + COMMAND_SEPARATOR + topicEntity.getId()));
		}
		inlineKeyboard.addRow().add(new KeyboardItem(CANCEL_TEXT, CANCEL_STRING));
		return inlineKeyboard;
	}

	private SendMessage listTopicsToUnsubscribe(final UserEntity userEntity) {
		var subscriptionList = subscriptionRepository.findUserTopics(userEntity);

		var inlineKeyboard = createTopicKeyboard(subscriptionList, UNSUBSCRIBE_KEYWORD);

		return new SendMessage(userEntity.getId(), translate(userEntity, "UNSUBSCRIBE_LIST_HEADER")).keyboard(inlineKeyboard);
	}

	private SendMessage subscribe(UserEntity userEntity, final String topic) {
		try {
			saveSubscription(userEntity, topic);

			telegramService.sendCommand(new SendMessage(userEntity.getId(), topic + " -> " + translate(userEntity, "SUBSCRIBE_SUCCESS")));
			return listTopicsToSubscribe(userEntity);
		} catch (JpaObjectRetrievalFailureException e) {
			LOGGER.error("attempted to subscribe non-existing {}", topic);
			return new SendMessage(userEntity.getId(),
					topic + " -> " + translate(userEntity, translate(userEntity, "TOPIC_NOT_EXISTS")));
		} catch (Exception e) {
			LOGGER.error("Unhandled error in subscribe method", e);
			telegramService.sendMessageToMaster(userEntity.getFullName() + " (" + userEntity.getId() + ") " + topic + " abone olmaya çalıştı");
			return new SendMessage(userEntity.getId(),
					topic + " -> " + translate(userEntity, translate(userEntity, "SUBSCRIBE_FAIL") + " (" + e.getClass() + ")"));
		}
	}

	private void saveSubscription(UserEntity userEntity, final String topic) {
		final var subscription = new SubscriptionEntity();

		final var topicObj = new TopicEntity();
		topicObj.setId(topic);

		subscription.setUser(userEntity);
		subscription.setTopic(topicObj);

		subscriptionRepository.save(subscription);
	}

	private SendMessage unsubscribe(UserEntity userEntity, final String topic) {
		var subscription = new SubscriptionEntity();
		subscription.setUser(userEntity);

		var topicObj = new TopicEntity();
		topicObj.setId(topic);
		subscription.setTopic(topicObj);

		try {
			subscriptionRepository.delete(subscription);

			telegramService.sendCommand(new SendMessage(userEntity.getId(), topic + " -> " + translate(userEntity, "UNSUBSCRIBE_SUCCESS")));
			return listTopicsToUnsubscribe(userEntity);
		} catch (Exception e) {
			LOGGER.error("Unhandled error in unsubscribe method", e);
			return new SendMessage(userEntity.getId(), topic + " -> " + translate(userEntity, "UNSUBSCRIBE_FAIL"));
		}
	}

	/**
	 * @deprecated TranslatorService oluşturup oradan çağır
	 */
	@Deprecated
	private String translate(final UserEntity userEntity, final String sentence) {
		return dictionaryKeeper.getTranslation(userEntity.getLanguage(), sentence);
	}

}
