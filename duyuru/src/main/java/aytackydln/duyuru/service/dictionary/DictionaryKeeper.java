package aytackydln.duyuru.service.dictionary;

import aytackydln.duyuru.setting.ConfigurationSet;
import aytackydln.duyuru.jpa.models.TranslationEntity;
import aytackydln.duyuru.jpa.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DictionaryKeeper implements InitializingBean {
	private final HashMap<String, Dictionary> dictionaries = new HashMap<>();

	private final TranslationRepository translationRepository;
	private final ConfigurationSet configurationSet;

	@Override
	public void afterPropertiesSet() {
		updateTranslations();
	}

	public void updateTranslations() {
		final List<String> languages = translationRepository.getLanguages();
		final List<String> sentences = translationRepository.getSentences();

		for (final String language : languages) {
			final Dictionary dictionary = new Dictionary(language);
			for (final String sentence : sentences) {
				final TranslationEntity translationEntity =translationRepository.getByLanguageAndSentence(language,sentence);
				if(translationEntity !=null){
					dictionary.addString(translationEntity);
				}else {
					final TranslationEntity undefinedTranslationEntity =new TranslationEntity();
					undefinedTranslationEntity.setLanguage(language);
					undefinedTranslationEntity.setSentence(sentence);
					undefinedTranslationEntity.setText(sentence);
					dictionary.addString(undefinedTranslationEntity);
					translationRepository.save(undefinedTranslationEntity);
				}
				dictionaries.put(language,dictionary);
			}
		}
	}

	public final String getTranslation(final String language,final String sentence){
		try {
			String result;
			if(dictionaries.containsKey(language))
				result=dictionaries.get(language).getString(sentence);
			else result=dictionaries.get(configurationSet.getDefaultLanguage()).getString(sentence);
			if(result==null) {
				LOGGER.warn("untranslated sentence: {} language: {}", sentence, language);
				return sentence + " (UNTRANSLATED)";
			}
			return result;
		}catch(NullPointerException ignored) {
			LOGGER.warn("untranslated sentence: {} language: {}", sentence, language);
			return sentence + " (UNTRANSLATED)";
		}
	}
}
