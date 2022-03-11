package aytackydln.duyuru.dictionary;

import aytackydln.duyuru.jpa.entity.TranslationEntity;
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

	void addString(TranslationEntity translationEntity) {
		sentences.put(translationEntity.getSentence(), translationEntity.getText());
	}

	String getString(String sentence) {
		return sentences.get(sentence);
	}
}
