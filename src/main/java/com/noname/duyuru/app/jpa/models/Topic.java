package com.noname.duyuru.app.jpa.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//TODO cachable on existsById?
public class Topic implements Serializable {
	private String id;
	private String baseLink;
	private String boardAppend;
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

    @Id
    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public String getBaseLink() {
        return baseLink;
    }

    public void setBaseLink(String baseLink) {
        this.baseLink = baseLink;
    }

    public String getBoardAppend() {
        return boardAppend;
    }

    public void setBoardAppend(String boardAppend) {
        this.boardAppend = boardAppend;
    }

    @Enumerated(EnumType.STRING)
    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    @Transient
    public final String getAnnouncementLink() {
        return getBaseLink() + getBoardAppend();
    }
}
