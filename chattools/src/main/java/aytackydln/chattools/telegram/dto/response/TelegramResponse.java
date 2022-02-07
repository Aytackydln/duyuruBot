package aytackydln.chattools.telegram.dto.response;

import aytackydln.chattools.telegram.exception.TelegramException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

public interface TelegramResponse {
    String getMethod();

    @JsonIgnore
    boolean isLimited();

    @Nullable
    TelegramResponse onError(TelegramException e);
    //TODO TelegramResponse onError(Exception e, ServiceContext serviceContext);

    void preSend();
}
