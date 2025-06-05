package aytackydln.chattools.telegram

import aytackydln.chattools.Chat
import aytackydln.chattools.ChatPlatform
import aytackydln.chattools.ChatUser
import aytackydln.chattools.OutgoingMessage
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Getter
import lombok.Setter
import lombok.ToString

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TelegramChat(
    val id: Long,
    override val platformId: Long = 0,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    @JsonProperty("language_code")
    val language: String? = null,
) : Chat, ChatUser {

    @get:ToString.Include
    val fullName: String
        get() = "$firstName $lastName"
    override val platformChatId: Long
        get() = id

    override val platform: ChatPlatform
        get() = ChatPlatform.TELEGRAM

    override fun sendMessage(message: OutgoingMessage<*>) {
        throw UnsupportedOperationException("TelegramChat.sendMessage")
    }
}
