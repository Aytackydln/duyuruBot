package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboard implements Keyboard {
    private final List<List<KeyboardItem>> keyboard = new ArrayList<>();

    @JsonProperty("resize_keyboard")
    public boolean isResizeKeyboard() {
        return true;
    }

    @JsonProperty("one_time_keyboard")
    public boolean isOneTimeKeyboard() {
        return true;
    }

    public List<List<KeyboardItem>> getKeyboard() {
        return keyboard;
	}

	public List<KeyboardItem> addRow(){
		List<KeyboardItem> newList=new ArrayList<>();
		keyboard.add(newList);
		return newList;
	}
}
