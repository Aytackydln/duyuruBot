package aytackydln.chattools.telegram.dto.response;

import aytackydln.chattools.telegram.exception.TelegramException;
import aytackydln.chattools.telegram.dto.models.Keyboard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@ToString(onlyExplicitlyIncluded = true)
public class SendMessage implements TelegramResponse {

    @ToString.Include
    private final long chatId;

    @ToString.Include
    private String text;
    private boolean disableNotification;
    private Keyboard replyMarkup;

    private boolean errorProcessed = false;

    public SendMessage(final long chatId, final String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public SendMessage keyboard(final Keyboard keyboard) {
        replyMarkup = keyboard;
        return this;
    }

    public SendMessage silent() {
        disableNotification = true;
        return this;
    }

    public String getMethod() {
        return "sendMessage";
    }

    @JsonProperty
    public String getParseMode() {
        return "HTML";
    }

    @Override
    @JsonIgnore
    public boolean isLimited() {
        return true;
    }

    @Override
    public TelegramResponse onError(TelegramException e) {
        if (!errorProcessed) {
            errorProcessed = true;
            return this;
        }
        return null;
    }

    @Override
    public void preSend() {
        //dont make notification sound between 00:00-07:00
        final var calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(new Date());   // assigns calendar to given date
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 7) {
            silent();
        }
    }
}
