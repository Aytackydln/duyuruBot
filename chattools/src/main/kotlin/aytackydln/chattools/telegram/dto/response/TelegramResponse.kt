package aytackydln.chattools.telegram.dto.response

import aytackydln.chattools.telegram.exception.TelegramException
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.lang.Nullable

interface TelegramResponse {
    val method: String

    @get:JsonIgnore
    val isLimited: Boolean

    @Nullable
    fun onError(e: TelegramException): TelegramResponse?

    //TODO TelegramResponse onError(Exception e, ServiceContext serviceContext);
    fun preSend()
}
