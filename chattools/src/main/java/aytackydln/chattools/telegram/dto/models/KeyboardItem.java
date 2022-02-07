package aytackydln.chattools.telegram.dto.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@RequiredArgsConstructor
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
