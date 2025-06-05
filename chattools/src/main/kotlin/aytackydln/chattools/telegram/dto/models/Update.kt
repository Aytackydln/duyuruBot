package aytackydln.chattools.telegram.dto.models

import aytackydln.chattools.telegram.TelegramChat
import aytackydln.chattools.telegram.TelegramMessage
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data
import lombok.RequiredArgsConstructor
import lombok.extern.jackson.Jacksonized

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Update(
    val updateId: Long,
    val message: TelegramMessage? = null,
    val callbackQuery: CallbackQuery? = null,
    val myChatMember: ChatDetails? = null,
) {

    @get:JsonIgnore
    val user: TelegramChat?
        get() {
            if (message != null) {
                return message.user as TelegramChat?
            } else if (callbackQuery != null) {
                return callbackQuery.from
            } else if (myChatMember != null) {
                return myChatMember.chat
            }
            throw IllegalArgumentException("User could not be found from update")
        }
}
