package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noname.duyuru.app.json.models.Chat;

import javax.persistence.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long messageId;

	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	private String text;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Calendar time;

	@Transient
	public Chat chat;

	@PrePersist
	void preInsert() {
		if (time == null)
			time = new GregorianCalendar();
	}

	@JsonProperty("message_id")
	public long getMessageId() {
		return messageId;
	}

	@JsonProperty("message_id")
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@JsonProperty("from")
	public User getUser() {
		return user;
	}

	@JsonProperty("from")
	public void setUser(User user) {
		this.user = user;
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}
}
