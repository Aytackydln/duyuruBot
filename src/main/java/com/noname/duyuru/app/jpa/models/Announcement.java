package com.noname.duyuru.app.jpa.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.noname.duyuru.app.jpa.repositories.AnnouncementKey;

@Entity
@IdClass(AnnouncementKey.class)
public class Announcement implements Serializable {
	private Topic topic;
	private String title;
	private Date date;
	private String link;

	@Override
	public String toString() {
		return "<b>" + topic.toString() + "</b>\n<a href=\"" + getUrl() + "\">" + title + "</a>";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Announcement that = (Announcement) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Transient
	public AnnouncementKey getId(){
		return new AnnouncementKey(topic.getId(), title, link);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Id
	@ManyToOne
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	@Id
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Transient
	public String getUrl(){
		return topic.getBaseLink() + link;
	}
}
