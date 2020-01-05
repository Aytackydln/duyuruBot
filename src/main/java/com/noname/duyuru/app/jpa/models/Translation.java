package com.noname.duyuru.app.jpa.models;

import com.noname.duyuru.app.jpa.repositories.TranslationKey;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(TranslationKey.class)
public class Translation{
	@Id
	private String language;
	@Id
	private String sentence;
	private String text;

	public String getLanguage(){
		return language;
	}

	public void setLanguage(String language){
		this.language=language;
	}

	public String getSentence(){
		return sentence;
	}

	public void setSentence(String sentence){
		this.sentence=sentence;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text=text;
	}
}
