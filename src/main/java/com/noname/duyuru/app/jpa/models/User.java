package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable{
	@Id
	private long id;
	@Column(name="name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String username;
	@Column(name="language")
	private String languageCode;
	@OneToMany(mappedBy="user", cascade={CascadeType.PERSIST})
	private List<Subscription> subscriptions;

	@Override
	public boolean equals(final Object obj){
		if(super.equals(obj)){
			return true;
		}else{
			if(obj instanceof User){
				final User otherUser=((User) obj);
				return id==otherUser.id;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		return ((int) id);
	}

	@JsonProperty("language_code")
	public String getLanguage(){
		return languageCode;
	}

	@JsonProperty("language_code")
	public void setLanguage(String language){
		this.languageCode=language;
	}

	@JsonProperty("first_name")
	public String getFirstName(){
		return firstName;
	}

	@JsonProperty("first_name")
	public void setName(String firstName){
		this.firstName=firstName;
	}

	@JsonProperty("last_name")
	public String getLastName(){
		return lastName;
	}

	@JsonProperty("last_name")
	public void setLastName(String lastName){
		this.lastName=lastName;
	}

	public String getFullName(){
		return getFirstName()+" "+getLastName();
	}

	@JsonProperty("username")
	public String getUsername(){
		return username;
	}

	@JsonProperty("username")
	public void setUsername(String username){
		this.username=username;
	}

	@JsonProperty("id")
	public long getId(){
		return id;
	}

	@JsonProperty("id")
	public void setId(long id){
		this.id=id;
	}

	public void setSubscriptions(List<Subscription> subscriptions){
		this.subscriptions=subscriptions;
	}

	public List<Subscription> getSubscriptions(){
		return subscriptions;
	}
}
