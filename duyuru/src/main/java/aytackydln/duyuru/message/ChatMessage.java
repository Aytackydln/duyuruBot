package aytackydln.duyuru.message;

import aytackydln.chattools.telegram.dto.models.Chat;
import aytackydln.duyuru.subscriber.Subscriber;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChatMessage {
	private long messageId;
	private Subscriber user;
	private String text;
    private Instant time;

    public Chat chat;
}
