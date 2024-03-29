package aytackydln.duyuru.mvc.message;

import lombok.Value;

@Value
public class InfoMessage implements ViewMessage {
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
