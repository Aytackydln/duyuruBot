package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noname.duyuru.app.component.ServiceContext;
import org.springframework.lang.Nullable;

public interface TelegramResponse {
    String getMethod();

    @JsonIgnore
    boolean isLimited();

    @Nullable
    TelegramResponse onError(Exception e, ServiceContext serviceContext);

    void preSend();
}
