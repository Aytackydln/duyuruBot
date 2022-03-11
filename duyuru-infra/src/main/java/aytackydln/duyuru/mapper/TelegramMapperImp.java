package aytackydln.duyuru.mapper;

import aytackydln.chattools.telegram.TelegramChat;
import aytackydln.chattools.telegram.TelegramMessage;
import aytackydln.chattools.telegram.dto.models.Update;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.message.ChatMessage;
import aytackydln.duyuru.subscriber.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DuyuruMapperConfig.class)
public interface TelegramMapperImp extends TelegramMapper {
    ChatMessage toMessage(TelegramMessage update);

    @Mapping(target = "time", ignore = true)  //TODO now()
    @Mapping(target = "chat", ignore = true)
    @Mapping(target = "user", source = "callbackQuery.from")
    @Mapping(target = "messageId", source = "callbackQuery.message.messageId")
    @Mapping(target = "text", source = "callbackQuery.data")
    ChatMessage toMessageFromCallback(Update update);

    @Mapping(target = "subscriptions", ignore = true)
    Subscriber toUser(TelegramChat telegramChat);
}

