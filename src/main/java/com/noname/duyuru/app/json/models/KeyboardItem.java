package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
public class KeyboardItem{
	private String text;
	private String callbackData;

	public KeyboardItem(String text){
		this.text=text;
	}

	public String getJson(){
		return "[{ \"text\": \""+text+"\", \"callback_data\": \""+callbackData+"\" }],";
	}
}
