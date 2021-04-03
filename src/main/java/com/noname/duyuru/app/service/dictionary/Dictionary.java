package com.noname.duyuru.app.service.dictionary;

import com.noname.duyuru.app.jpa.models.Translation;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dictionary {
	private final Map<String, String> sentences = new HashMap<>();

	@EqualsAndHashCode.Include
	private final String language;

	void addString(Translation translation) {
		sentences.put(translation.getSentence(), translation.getText());
	}

	String getString(String sentence) {
		return sentences.get(sentence);
	}
}
