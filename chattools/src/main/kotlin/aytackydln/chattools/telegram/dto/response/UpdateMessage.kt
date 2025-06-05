package aytackydln.chattools.telegram.dto.response

import aytackydln.chattools.telegram.exception.TelegramException
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.AllArgsConstructor
import lombok.Data
import lombok.ToString

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@ToString
data class UpdateMessage(
    val messageId: Long = 0,
    @get:JsonUnwrapped val sendMessage: SendMessage,
) : TelegramResponse {

    override val method: String
        get() = "editMessageText"

    override val isLimited: Boolean
        get() = false

    override fun onError(e: TelegramException): TelegramResponse? {
        return null
    }

    override fun preSend() {
        //doesn't need
    }
}
