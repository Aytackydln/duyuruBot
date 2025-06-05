package aytackydln.chattools.telegram.dto.response

import aytackydln.chattools.telegram.dto.models.Keyboard
import aytackydln.chattools.telegram.exception.TelegramException
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data
import lombok.ToString
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Data
@ToString(onlyExplicitlyIncluded = true)
data class SendMessage(
    @field:ToString.Include val chatId: Long,
    @field:ToString.Include val text: String
) :
    TelegramResponse {
    var disableNotification = false
    var replyMarkup: Keyboard? = null

    var errorProcessed = false

    fun keyboard(keyboard: Keyboard?): SendMessage {
        replyMarkup = keyboard
        return this
    }

    fun silent(): SendMessage {
        disableNotification = true
        return this
    }

    override val method: String
        get() = "sendMessage"

    @get:JsonProperty
    val parseMode: String
        get() = "HTML"

    @get:JsonIgnore
    override val isLimited: Boolean
        get() = true

    override fun onError(e: TelegramException): TelegramResponse? {
        if (!errorProcessed) {
            errorProcessed = true
            return this
        }
        return null
    }

    override fun preSend() {
        //dont make notification sound between 00:00-07:00
        val calendar = Calendar.getInstance() // creates a new calendar instance
        calendar.setTime(Date()) // assigns calendar to given date
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        if (hour < 7) {
            silent()
        }
    }
}
