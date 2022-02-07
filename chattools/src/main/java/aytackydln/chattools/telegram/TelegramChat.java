package aytackydln.chattools.telegram;

import aytackydln.chattools.Chat;
import aytackydln.chattools.OutgoingMessage;
import aytackydln.chattools.ChatPlatform;
import aytackydln.chattools.ChatUser;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TelegramChat implements Chat, ChatUser {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String language;
    private String status;

    @ToString.Include
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public Long getPlatformChatId() {
        return id;
    }

    @Override
    public Long getPlatformId() {
        return id;
    }

    @Override
    public ChatPlatform getPlatform() {
        return ChatPlatform.TELEGRAM;
    }

    @Override
    public void sendMessage(OutgoingMessage<?> message) {
        throw new UnsupportedOperationException("TelegramChat.sendMessage");
    }
}
