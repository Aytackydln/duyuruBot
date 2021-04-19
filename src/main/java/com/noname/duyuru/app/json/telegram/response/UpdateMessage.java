package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@ToString
public class UpdateMessage implements TelegramResponse {
    private long messageId;

    private SendMessage sendMessage;

    @JsonUnwrapped
    public SendMessage getSendMessage() {
        return sendMessage;
    }

    @Override
    public String getMethod() {
        return "editMessageText";
    }

    @Override
    public boolean isLimited() {
        return false;
    }

    @Override
    public TelegramResponse onError(Exception e) {
        return null;
    }

    @Override
    public void preSend() {
        //doesn't need
    }
}
