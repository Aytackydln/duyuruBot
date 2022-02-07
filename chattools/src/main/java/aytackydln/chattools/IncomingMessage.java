package aytackydln.chattools;

import java.util.Map;

public interface IncomingMessage<P, C extends Chat> {
    C getChat();
    void setChat(C chat);

    ChatUser getUser();
    void setUser(ChatUser chatUser);

    Map<String, P> getPlatformMessageProperties();
    void getPlatformMessageProperties(Map<String, P> platformProperties);
}
