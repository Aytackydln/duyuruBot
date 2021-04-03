package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.json.models.Keyboard;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class UpdateMessage implements TelegramResponse {
    private long chatId;
    private long messageId;
    private String text;
    private Keyboard replyMarkup;

    @Override
    public String getMethod() {
        return "editMessageText";
    }

    @JsonProperty("parse_mode")
    public String getParseMode() {
        return "HTML";
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
