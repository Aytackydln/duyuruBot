package com.noname.duyuru.app.jpa.repositories;

import java.io.Serializable;
import java.util.Objects;

public class TranslationKey implements Serializable{
	private String language;
	private String sentence;

	@Override
	public boolean equals(Object o){
		if(this==o) return true;
		if(o==null||getClass()!=o.getClass()) return false;
		TranslationKey that=(TranslationKey)o;
		return language.equals(that.language)&&sentence.equals(that.sentence);
	}

	@Override
	public int hashCode(){
		return Objects.hash(language,sentence);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
}
