package aytackydln.duyuru.chatplatform.port;

import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import aytackydln.duyuru.subscriber.Subscriber;

public interface TelegramPort {
    void sendCommand(TelegramResponse response);

    void sendMessage(Subscriber subscriber, String message);
    void deleteMessage(Subscriber subscriber, long messageId);
}
