package aytackydln.chattools.telegram.dto.models

import aytackydln.chattools.telegram.TelegramChat
import aytackydln.chattools.telegram.TelegramMessage
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data
import lombok.RequiredArgsConstructor

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CallbackQuery (
    val id: String,
    val message: TelegramMessage? = null,
    val data: String? = null,
    val from: TelegramChat,
    val inlineMessageId: String? = null,
)
