package aytackydln.chattools

interface ChatUser {
    val platformId: Long
    val platform: ChatPlatform

    fun sendMessage(message: OutgoingMessage<*>)
}
