package aytackydln.duyuru.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Transient;
import lombok.*;

import java.io.Serializable;

@Entity(name = "Translation")
@IdClass(TranslationEntity.Key.class)
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TranslationEntity {
    private final TranslationEntity.Key id = new TranslationEntity.Key();
    private String language;
    private String sentence;
    private String text;

    @Transient
    @EqualsAndHashCode.Include
    @ToString.Include
    public TranslationEntity.Key getId() {
        return id;
    }

    @Id
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        id.setLanguage(language);
        this.language = language;
    }

    @Id
    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        id.setSentence(sentence);
        this.sentence = sentence;
    }

    @ToString.Include
    public String getText() {
        return text;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Key implements Serializable {
        private String language;
        private String sentence;
    }
}
