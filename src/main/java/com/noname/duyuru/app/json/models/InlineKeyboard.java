package com.noname.duyuru.app.json.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InlineKeyboard extends Keyboard {
	private final List<List<KeyboardItem>> content=new ArrayList<>();

	@JsonProperty("inline_keyboard")
	public List<List<KeyboardItem>> getContent(){
		return content;
	}

	@JsonProperty("one_time_keyboard")
	public boolean isOneTimeKeyboard(){return true;}

	public List<KeyboardItem> addRow(){
		List<KeyboardItem> newList=new ArrayList<>();
		content.add(newList);
		return newList;
	}
}
