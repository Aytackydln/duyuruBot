package aytackydln.duyuru.jpa.entity;

import aytackydln.chattools.telegram.dto.models.Chat;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity(name = "Message")
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class MessageEntity {
	private Long messageId;
	private UserEntity user;
	private String text;
    private Instant time;

    public Chat chat;

    @PrePersist
    void preInsert() {
        if (time == null)
            time = Instant.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonAlias("message_id")
    @EqualsAndHashCode.Include
    public Long getMessageId() {
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
    public Instant getTime() {
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
