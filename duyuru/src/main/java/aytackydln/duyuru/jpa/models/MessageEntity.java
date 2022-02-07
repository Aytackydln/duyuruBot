package aytackydln.duyuru.jpa.models;

import aytackydln.chattools.telegram.dto.models.Chat;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

@Entity(name = "Message")
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class MessageEntity {
	private long messageId;
	private UserEntity user;
	private String text;
    private Calendar time;

    public Chat chat;

    @PrePersist
    void preInsert() {
        if (time == null)
            time = new GregorianCalendar();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonAlias("message_id")
    @EqualsAndHashCode.Include
    public long getMessageId() {
        return messageId;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JsonProperty("from")
    public UserEntity getUser() {
        return user;
    }

    @JsonAlias("text")
    public String getText() {
        return text;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Calendar getTime() {
        return time;
    }

    @Transient
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
