package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Chat;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Message {
	private long messageId;
	private User user;
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
    @ToString.Include
    public User getUser() {
        return user;
    }

    @JsonAlias("text")
    @ToString.Include
    public String getText() {
        return text;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @ToString.Include
    public Calendar getTime() {
        return time;
    }
}
