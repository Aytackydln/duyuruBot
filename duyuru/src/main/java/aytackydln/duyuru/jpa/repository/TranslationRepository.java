package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.models.TranslationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends CrudRepository<TranslationEntity, TranslationEntity.Key> {
    TranslationEntity getByLanguageAndSentence(String language, String sentence);

    @Query("select distinct t.language from Translation t")
    List<String> getLanguages();

    @Query("select distinct t.sentence from Translation t")
    List<String> getSentences();
}
