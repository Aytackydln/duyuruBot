package aytackydln.chattools.telegram.dto.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InlineKeyboard implements Keyboard {
    private final List<List<KeyboardItem>> content = new ArrayList<>();

    @JsonProperty("inline_keyboard")
    public List<List<KeyboardItem>> getContent() {
        return content;
    }

    @JsonProperty
    public boolean isOneTimeKeyboard() {
        return true;
    }

    public List<KeyboardItem> addRow(){
        List<KeyboardItem> newList=new ArrayList<>();
        content.add(newList);
        return newList;
    }
}
