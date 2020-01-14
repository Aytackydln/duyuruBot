package com.noname.duyuru.app.jpa.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(AnnouncementKey.class)
public class Announcement implements Serializable {
    private final AnnouncementKey id = new AnnouncementKey();
    private Topic topic;
    private String title;
    private String link;
    private Date date;

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
    public AnnouncementKey getId() {
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
	public String getUrl(){
		return topic.getBaseLink() + link;
	}
}
