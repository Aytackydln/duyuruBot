package com.noname.duyuru.app.service.dictionary;

import com.noname.duyuru.app.jpa.models.Translation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Dictionary {
	private final Map<String, String> sentences = new HashMap<>();
	private final String language;

	Dictionary(String language) {
		this.language = language;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Dictionary that=(Dictionary)o;
		return language.equals(that.language);
	}

	@Override
	public int hashCode(){
		return Objects.hash(language);
	}

	void addString(Translation translation){
		sentences.put(translation.getSentence(),translation.getText());
	}

	String getString(String sentence){
		return sentences.get(sentence);
	}
}
