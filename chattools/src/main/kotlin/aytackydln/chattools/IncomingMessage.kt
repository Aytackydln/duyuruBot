package aytackydln.chattools

interface IncomingMessage<P, C : Chat> {
    var chat: C

    var user: C

    val platformMessageProperties: MutableMap<String, P>
    fun getPlatformMessageProperties(platformProperties: MutableMap<String, P>)
}
