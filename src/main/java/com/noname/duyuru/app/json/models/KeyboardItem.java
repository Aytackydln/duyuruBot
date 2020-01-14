package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyboardItem{
	private String text;
	private String callbackData;

	public KeyboardItem(String text){
		this.text=text;
	}

	public KeyboardItem(String text, String callbackData){
		this.text=text;
		this.callbackData=callbackData;
	}

	public String getJson(){
		return "[{ \"text\": \""+text+"\", \"callback_data\": \""+callbackData+"\" }],";
	}

	@JsonProperty("text")
	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text=text;
	}

	@JsonProperty("callback_data")
	public String getCallbackData(){
		return callbackData;
	}

	public void setCallbackData(String callbackData){
		this.callbackData=callbackData;
	}
}
