package aytackydln.duyuru.mapper;

import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.message.ChatMessage;
import aytackydln.duyuru.jpa.entity.MessageEntity;
import org.mapstruct.Mapper;

@Mapper(config = DuyuruMapperConfig.class)
public interface MessageMapper {
    MessageEntity map(ChatMessage chatMessage);
    ChatMessage mapFromEntity(MessageEntity messageEntity);
}
