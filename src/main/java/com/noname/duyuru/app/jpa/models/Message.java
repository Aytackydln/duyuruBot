package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Chat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity
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
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JsonProperty("from")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonAlias("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}
