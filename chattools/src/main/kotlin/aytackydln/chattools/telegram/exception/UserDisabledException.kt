package aytackydln.chattools.telegram.exception

import lombok.EqualsAndHashCode
import lombok.Value

@Value
@EqualsAndHashCode(callSuper = false)
class UserDisabledException(
    var userId: Long
) : TelegramException()
