package aytackydln.duyuru.jpa.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Announcement")
@IdClass(AnnouncementEntity.Key.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnnouncementEntity implements Serializable {
    private final AnnouncementEntity.Key id = new AnnouncementEntity.Key();
    private TopicEntity topic;
    private String title;
    private String link;
    private Date date;

    @Override
    public String toString() {
        return "<b>" + topic.toString() + "</b>\n<a href=\"" + getUrl() + "\">" + title + "</a>";
    }

    @Transient
    @EqualsAndHashCode.Include
    public AnnouncementEntity.Key getId() {
        return id;
    }

    @Id
    @ManyToOne(optional = false)
    public TopicEntity getTopic() {
		return topic;
	}

	public void setTopic(TopicEntity topicEntity) {
        if (topicEntity != null) id.setTopic(topicEntity.getId());
        else id.setTopic(null);
        this.topic = topicEntity;
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
