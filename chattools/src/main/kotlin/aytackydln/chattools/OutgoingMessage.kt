package aytackydln.chattools

interface OutgoingMessage<P> {
    var text: String?

    val platformMessageProperties: MutableMap<String, P>
    fun getPlatformMessageProperties(platformProperties: MutableMap<String, P>)
}