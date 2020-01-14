package com.noname.duyuru.app.jpa.models;

import java.io.Serializable;
import java.util.Objects;

public class AnnouncementKey implements Serializable{
	private String topic;
	private String title;
	private String link;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic){
		this.topic=topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnouncementKey that = (AnnouncementKey) o;
        return topic.equals(that.topic) &&
                title.equals(that.title) &&
                link.equals(that.link);
    }

    @Override
    public int hashCode() {
        if (link.equals(""))
            return Objects.hash(topic, title);
        return Objects.hash(topic, title, link);
    }

    @Override
    public String toString() {
        return "AnnouncementKey{" +
                "topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}