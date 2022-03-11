package aytackydln.duyuru.adapter;

import aytackydln.duyuru.common.TranslationPort;
import aytackydln.duyuru.dictionary.DictionaryKeeper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslationAdapter implements TranslationPort {
    private final DictionaryKeeper dictionaryKeeper;

    @Override
    public String translate(String sentence, String language) {
        return dictionaryKeeper.getTranslation(language, sentence);
    }
}
