package aytackydln.chattools;

import java.util.Map;

public interface OutgoingMessage<P> {
    String getText();
    void setText(String text);

    Map<String, P> getPlatformMessageProperties();
    void getPlatformMessageProperties(Map<String, P> platformProperties);
}
