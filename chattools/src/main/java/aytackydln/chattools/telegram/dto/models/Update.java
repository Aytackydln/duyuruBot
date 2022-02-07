package aytackydln.chattools.telegram.dto.models;

import aytackydln.chattools.telegram.TelegramChat;
import aytackydln.chattools.telegram.TelegramMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@RequiredArgsConstructor
public class Update {
    private int updateId;
    private TelegramMessage message;
    private CallbackQuery callbackQuery;
    private ChatDetails myChatMember;

    @JsonIgnore
    public TelegramChat getUser() {
        if (message != null) {
            return message.getUser();
        } else if (callbackQuery != null) {
            return callbackQuery.getFrom();
        } else if (myChatMember != null) {
            return myChatMember.getChat();
        }
        throw new IllegalArgumentException("User could not be found from update");
    }
}
