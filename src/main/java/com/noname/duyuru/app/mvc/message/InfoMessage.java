package com.noname.duyuru.app.mvc.message;

import lombok.Value;

@Value
public class InfoMessage implements IViewMessage {
    String message;

    @Override
    public final String getHtmlClass() {
        return "alert-info";
    }

    @Override
    public String getHeader() {
        return "Info!";
    }
}
