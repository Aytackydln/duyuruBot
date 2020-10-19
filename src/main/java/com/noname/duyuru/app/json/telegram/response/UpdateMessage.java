package com.noname.duyuru.app.json.telegram.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Keyboard;
import org.springframework.web.client.HttpClientErrorException;

public class UpdateMessage implements TelegramResponse {
    private long chatId;
    private long messageId;
    private String text;
    private Keyboard replyMarkup;

    @Override
    public String getMethod() {
        return "editMessageText";
    }

    @JsonProperty("chat_id")
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @JsonProperty("message_id")
    public long getMessageId() {
        return messageId;
    }

    @JsonProperty("message_id")
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("parse_mode")
    public String getParseMode() {
        return "HTML";
    }

    @JsonProperty("reply_markup")
    public Keyboard getReplyMarkup() {
        return replyMarkup;
    }

    @JsonProperty("reply_markup")
    public void setReplyMarkup(Keyboard replyMarkup) {
        this.replyMarkup = replyMarkup;
    }

    @Override
    public boolean isLimited() {
        return false;
    }

    @Override
    public TelegramResponse onError(HttpClientErrorException e) {
        return null;
    }
}
