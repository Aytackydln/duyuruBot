package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends CrudRepository<Translation,String> {
	Translation getByLanguageAndSentence(String language,String sentence);

	@Query("select distinct t.language from Translation t")
	List<String> getLanguages();

	@Query("select distinct t.sentence from Translation t")
	List<String> getSentences();
}
