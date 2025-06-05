package aytackydln.duyuru.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "Announcement")
@IdClass(AnnouncementEntity.Key.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class AnnouncementEntity implements Serializable {
    private final AnnouncementEntity.Key id = new AnnouncementEntity.Key();
    private TopicEntity topic;
    private String title;
    private String link;
    private Date date;

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

    public void setDate(Date date) {
        this.date = date;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode
    public static class Key implements Serializable {
        private String topic;
        private String title;
        private String link;
    }
}
