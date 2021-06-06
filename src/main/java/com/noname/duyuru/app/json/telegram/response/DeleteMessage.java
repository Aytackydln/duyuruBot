package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.component.ServiceContext;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@Log4j2
public class DeleteMessage implements TelegramResponse {

    public static final String ERROR_WHILE_SENDING_DELETE_MESSAGE_RESPONSE_CHAT_ID_MESSAGE_ID
            = "Error while sending delete message response: \n chatId: {} messageId: {}";
    private final long chatId;
    private final long messageId;

    private boolean errorProcessed = false;

    @Override
    public String getMethod() {
        return "deleteMessage";
    }

    @Override
    @JsonIgnore
    public boolean isLimited() {
        return false;
    }

    @Override
    public TelegramResponse onError(Exception e, ServiceContext serviceContext) {
        LOGGER.error(ERROR_WHILE_SENDING_DELETE_MESSAGE_RESPONSE_CHAT_ID_MESSAGE_ID, chatId, messageId);
        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException ce = (HttpClientErrorException) e;
            if (!errorProcessed) {
                errorProcessed = true;
            }
            LOGGER.trace(ce.getResponseBodyAsString());
        }

        return null;
    }

    @Override
    public void preSend() {
        //doesn't need
    }
}
