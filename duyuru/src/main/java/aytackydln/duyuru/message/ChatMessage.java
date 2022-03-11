package aytackydln.duyuru.message;

import aytackydln.chattools.telegram.dto.models.Chat;
import aytackydln.duyuru.subscriber.Subscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class ChatMessage {
	private long messageId;
	private Subscriber user;
	private String text;
    private Calendar time;

    public Chat chat;
}
