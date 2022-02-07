package aytackydln.chattools.telegram.dto.response;

import aytackydln.chattools.telegram.exception.TelegramException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
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
    public TelegramResponse onError(TelegramException e) {
            if (!errorProcessed) {
                errorProcessed = true;
            }
        return null;
    }

    @Override
    public void preSend() {
        //doesn't need
    }
}
