package aytackydln.chattools.telegram.dto.models;

import aytackydln.chattools.telegram.TelegramChat;
import aytackydln.chattools.telegram.TelegramMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@RequiredArgsConstructor
public class CallbackQuery{
	private String id;
	private TelegramMessage message;
	private String data;
	private TelegramChat from;
	private String inlineMessageId;
}
