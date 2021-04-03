package com.noname.duyuru.app.jpa.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@IdClass(Translation.Key.class)
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Translation {
    private final Translation.Key id = new Translation.Key();
    private String language;
    private String sentence;
    private String text;

    @Transient
    @EqualsAndHashCode.Include
    @ToString.Include
    public Translation.Key getId() {
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
