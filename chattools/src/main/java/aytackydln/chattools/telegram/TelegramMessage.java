package aytackydln.chattools.telegram;

import aytackydln.chattools.IncomingMessage;
import aytackydln.chattools.OutgoingMessage;
import aytackydln.chattools.ChatUser;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Calendar;
import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TelegramMessage implements IncomingMessage<Void, TelegramChat>, OutgoingMessage<Void> {

    @EqualsAndHashCode.Include
    private long messageId;

    @ToString.Include
    private TelegramChat user;

    @ToString.Include
    private String text;

    @ToString.Include
    private Calendar time;

    @Override
    public TelegramChat getChat() {
        return user;
    }

    @Override
    public void setChat(TelegramChat chat) {
        user = chat;
    }

    @Override
    public void setUser(ChatUser chatUser) {
        this.user = (TelegramChat) chatUser;
    }

    @Override
    public Map<String, Void> getPlatformMessageProperties() {
        return null;
    }

    @Override
    public void getPlatformMessageProperties(Map<String, Void> platformProperties) {

    }
}
