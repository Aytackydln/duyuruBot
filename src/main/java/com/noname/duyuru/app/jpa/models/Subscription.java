package com.noname.duyuru.app.jpa.models;

import com.noname.duyuru.app.jpa.repositories.SubscriptionKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SubscriptionKey.class)
public class Subscription implements Serializable{
	@Id
	@ManyToOne(cascade=CascadeType.MERGE)
	private User user;
	@Id
	@ManyToOne
	private Topic topic;


	public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user=user;
	}

	public Topic getTopic(){
		return topic;
	}

	public void setTopic(Topic topic){
		this.topic=topic;
	}
}
