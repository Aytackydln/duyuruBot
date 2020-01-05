package com.noname.duyuru.app.jpa.repositories;

import java.io.Serializable;
import java.util.Objects;

public class SubscriptionKey implements Serializable{
	private long user;
	private String topic;

	@Override
	public boolean equals(Object o){
		if(this==o) return true;
		if(o==null||getClass()!=o.getClass()) return false;
		SubscriptionKey that=(SubscriptionKey)o;
		return user==that.user&&topic.equals(that.topic);
	}

	@Override
	public int hashCode(){
		return Objects.hash(user,topic);
	}

	public long getUser(){
		return user;
	}

	public void setUser(int user){
		this.user=user;
	}

	public String getTopic(){
		return topic;
	}

	public void setTopic(String topic){
		this.topic=topic;
	}
}
