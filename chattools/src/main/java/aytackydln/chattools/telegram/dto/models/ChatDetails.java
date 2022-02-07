package aytackydln.chattools.telegram.dto.models;

import aytackydln.chattools.telegram.TelegramChat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@RequiredArgsConstructor
public class ChatDetails {
    private TelegramChat chat;
    private ChatMember newChatMember;
}
