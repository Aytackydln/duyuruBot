package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard implements Keyboard {
    private final List<List<KeyboardItem>> content = new ArrayList<>();

    @JsonProperty("inline_keyboard")
    public List<List<KeyboardItem>> getContent() {
        return content;
    }

    @JsonProperty("one_time_keyboard")
    public boolean isOneTimeKeyboard() {
        return true;
    }

	public List<KeyboardItem> addRow(){
		List<KeyboardItem> newList=new ArrayList<>();
		content.add(newList);
		return newList;
	}
}
