package com.noname.duyuru.app.service.dictionary;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.noname.duyuru.app.jpa.models.Translation;
import com.noname.duyuru.app.jpa.repositories.TranslationRepository;
import com.noname.duyuru.app.setting.ConfigurationSet;

@Service
public class DictionaryKeeper{
	private static final Logger LOGGER = LogManager.getLogger(DictionaryKeeper.class);

	private final TranslationRepository translationRepository;
	private final HashMap<String,Dictionary> dictionaries=new HashMap<>();

	private final ConfigurationSet configurationSet;

	public DictionaryKeeper(TranslationRepository translationRepository, ConfigurationSet configurationSet){
		this.translationRepository=translationRepository;
		this.configurationSet=configurationSet;

		updateTranslations();
	}

	public void updateTranslations() {
		final List<String> languages=translationRepository.getLanguages();
		final List<String> sentences=translationRepository.getSentences();

		for(final String language:languages){
			final Dictionary dictionary=new Dictionary(language);
			for(final String sentence:sentences){
				final Translation translation=translationRepository.getByLanguageAndSentence(language,sentence);
				if(translation!=null){
					dictionary.addString(translation);
				}else {
					final Translation undefinedTranslation=new Translation();
					undefinedTranslation.setLanguage(language);
					undefinedTranslation.setSentence(sentence);
					undefinedTranslation.setText(sentence);
					dictionary.addString(undefinedTranslation);
					translationRepository.save(undefinedTranslation);
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
			if(result==null){
				LOGGER.warn("untranslated sentence: "+sentence+" language: "+language);
				return sentence+" (UNTRANSLATED)";
			}
			return result;
		}catch(NullPointerException ignored){
			LOGGER.warn("untranslated sentence: "+sentence+" language: "+language);
			return sentence+" (UNTRANSLATED)";
		}
	}
}
