package aytackydln.duyuru.message.port;

import aytackydln.duyuru.message.ChatMessage;

public interface ChatMessagePort {
    void update(ChatMessage chatMessage);
}
