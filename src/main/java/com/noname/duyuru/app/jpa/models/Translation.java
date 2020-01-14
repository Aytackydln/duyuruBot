package com.noname.duyuru.app.jpa.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@IdClass(TranslationKey.class)
public class Translation {
    private final TranslationKey id = new TranslationKey();
    private String language;
    private String sentence;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Transient
    public TranslationKey getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
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

    public String getText() {
		return text;
	}

	public void setText(String text){
		this.text=text;
	}
}
