package com.noname.duyuru.app.jpa.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(Announcement.Key.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Announcement implements Serializable {
    private final Announcement.Key id = new Announcement.Key();
    private Topic topic;
    private String title;
    private String link;
    private Date date;

    @Override
    public String toString() {
        return "<b>" + topic.toString() + "</b>\n<a href=\"" + getUrl() + "\">" + title + "</a>";
    }

    @Transient
    @EqualsAndHashCode.Include
    public Announcement.Key getId() {
        return id;
    }

    @Id
    @ManyToOne(optional = false)
    public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
        if (topic != null) id.setTopic(topic.getId());
        else id.setTopic(null);
        this.topic = topic;
    }

    @Id
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        id.setTitle(title);
        this.title = title;
    }

    @Id
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        id.setLink(link);
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public String getUrl() {
        return topic.getBaseLink() + link;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode
    public static class Key implements Serializable {
        private String topic;
        private String title;
        private String link;
    }
}
