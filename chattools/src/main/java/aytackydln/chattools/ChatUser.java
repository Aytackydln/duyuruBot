package aytackydln.chattools;

public interface ChatUser {
    Long getPlatformId();
    ChatPlatform getPlatform();

    void sendMessage(OutgoingMessage<?> message);
}
