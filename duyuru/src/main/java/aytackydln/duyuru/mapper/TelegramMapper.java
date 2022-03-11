package aytackydln.duyuru.mapper;

import aytackydln.chattools.telegram.TelegramChat;
import aytackydln.chattools.telegram.TelegramMessage;
import aytackydln.chattools.telegram.dto.models.Update;
import aytackydln.duyuru.message.ChatMessage;
import aytackydln.duyuru.subscriber.Subscriber;

public interface TelegramMapper {
    ChatMessage toMessage(TelegramMessage update);
    ChatMessage toMessageFromCallback(Update update);
    Subscriber toUser(TelegramChat telegramChat);
}
