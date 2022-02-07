package aytackydln.chattools.telegram.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class UserDisabledException extends TelegramException {
    Long userId;
}
