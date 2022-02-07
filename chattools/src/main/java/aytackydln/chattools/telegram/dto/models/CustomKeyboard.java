package aytackydln.chattools.telegram.dto.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@RequiredArgsConstructor
public class CustomKeyboard implements Keyboard {
    private final List<List<KeyboardItem>> keyboard = new ArrayList<>();

    @JsonProperty
    public boolean isResizeKeyboard() {
        return true;
    }

    @JsonProperty
    public boolean isOneTimeKeyboard() {
        return true;
    }

    @JsonProperty
    public List<List<KeyboardItem>> getKeyboard() {
        return keyboard;
    }

    public List<KeyboardItem> addRow(){
        List<KeyboardItem> newList=new ArrayList<>();
        keyboard.add(newList);
        return newList;
    }
}
