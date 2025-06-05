package aytackydln.chattools.telegram

import aytackydln.chattools.IncomingMessage
import aytackydln.chattools.OutgoingMessage
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.EqualsAndHashCode
import lombok.RequiredArgsConstructor
import lombok.ToString
import java.time.Instant
import java.util.Collections.emptyMap

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class TelegramMessage : IncomingMessage<Any, TelegramChat>, OutgoingMessage<Any> {
    @EqualsAndHashCode.Include
    val messageId: Long = 0

    @ToString.Include
    override var text: String? = null

    @ToString.Include
    @JsonProperty("date")
    val time: Instant? = null

    private lateinit var innerUser: TelegramChat

    override var chat: TelegramChat
        get() = innerUser
        set(chat) {
            innerUser = chat
        }

    @get:ToString.Include
    override var user: TelegramChat
        get() = innerUser
        set(value) {
            innerUser = value
        }

    override val platformMessageProperties: MutableMap<String, Any>
        get() = emptyMap()

    override fun getPlatformMessageProperties(platformProperties: MutableMap<String, Any>) {
    }
}
