package aytackydln.duyuru.mapper;

import aytackydln.chattools.telegram.TelegramChat;
import aytackydln.chattools.telegram.TelegramMessage;
import aytackydln.chattools.telegram.dto.models.Update;
import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.models.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TelegramMapper {

    MessageEntity toMessage(TelegramMessage update);

    @Mapping(target = "time", ignore = true)  //TODO now()
    @Mapping(target = "chat", ignore = true)
    @Mapping(target = "user", source = "callbackQuery.from")
    @Mapping(target = "messageId", source = "callbackQuery.message.messageId")
    @Mapping(target = "text", source = "callbackQuery.data")
    MessageEntity toMessageFromCallback(Update update);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    UserEntity toUser(TelegramChat telegramChat);
}
