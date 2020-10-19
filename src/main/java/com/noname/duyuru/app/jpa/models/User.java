package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable{
	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String languageCode;
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
    public int hashCode() {
        return ((int) id);
    }

    @Id
    @JsonAlias("id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name")
    @JsonAlias("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    @JsonAlias("last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Transient
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "language")
    @JsonAlias("language_code")
    public String getLanguage() {
        return languageCode;
    }

    public void setLanguage(String language) {
        this.languageCode = language;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
