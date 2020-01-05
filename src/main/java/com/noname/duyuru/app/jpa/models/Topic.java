package com.noname.duyuru.app.jpa.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Topic implements Serializable {
	@Id
	private String id;
	private String baseLink;
	private String boardAppend;
	@Enumerated(EnumType.STRING)
	private TopicType type;
	//TODO subscribers ekle

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof Topic) {
			final Topic otherTopic = ((Topic) obj);
			return id.equals(otherTopic.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id;
	}

	public String getBoardAppend() {
		return boardAppend;
	}

	public String getBaseLink() {
		return baseLink;
	}

	public void setBaseLink(String baseLink) {
		this.baseLink = baseLink;
	}

	public void setBoardAppend(String boardAppend) {
		this.boardAppend = boardAppend;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getAnnouncementLink() {
		return getBaseLink() + getBoardAppend();
	}

	public void setType(TopicType type) {
		this.type = type;
	}

	public TopicType getType() {
		return type;
	}
}
