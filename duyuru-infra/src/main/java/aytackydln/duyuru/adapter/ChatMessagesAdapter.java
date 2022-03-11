package aytackydln.duyuru.adapter;

import aytackydln.duyuru.jpa.entity.MessageEntity;
import aytackydln.duyuru.jpa.repository.MessageRepository;
import aytackydln.duyuru.mapper.MessageMapper;
import aytackydln.duyuru.message.ChatMessage;
import aytackydln.duyuru.message.port.ChatMessagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMessagesAdapter implements ChatMessagePort {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public void update(ChatMessage chatMessage) {
        MessageEntity messageEntity = messageMapper.map(chatMessage);
        messageRepository.save(messageEntity);
    }
}
