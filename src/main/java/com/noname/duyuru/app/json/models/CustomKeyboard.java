package com.noname.duyuru.app.json.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomKeyboard extends Keyboard {
	private final List<List<KeyboardItem>> keyboard=new ArrayList<>();

	@JsonProperty("resize_keyboard")
	public boolean isResizeKeyboard(){return true;}

	@JsonProperty("one_time_keyboard")
	public boolean isOneTimeKeyboard(){return true;}

	public List<List<KeyboardItem>> getKeyboard(){
		return keyboard;
	}

	public List<KeyboardItem> addRow(){
		List<KeyboardItem> newList=new ArrayList<>();
		keyboard.add(newList);
		return newList;
	}
}
