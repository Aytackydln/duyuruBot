package aytackydln.chattools.telegram.dto.response

import aytackydln.chattools.telegram.exception.TelegramException
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DeleteMessage(
    val chatId: Long = 0,
    val messageId: Long = 0,
) : TelegramResponse {

    private var errorProcessed = false

    override val method: String
        get() = "deleteMessage"

    @get:JsonIgnore
    override val isLimited: Boolean
        get() = false

    override fun onError(e: TelegramException): TelegramResponse? {
        if (!errorProcessed) {
            errorProcessed = true
        }
        return null
    }

    override fun preSend() {
        //doesn't need
    }

    companion object {
        const val ERROR_WHILE_SENDING_DELETE_MESSAGE_RESPONSE_CHAT_ID_MESSAGE_ID
                : String = "Error while sending delete message response: \n chatId: {} messageId: {}"
    }
}
