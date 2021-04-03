package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
public class User implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String language;
    private List<Subscription> subscriptions;

    @Id
    @JsonAlias("id")
    @EqualsAndHashCode.Include
    public long getId() {
        return id;
    }

    @Column(name = "name")
    @JsonAlias("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    @JsonAlias("last_name")
    public String getLastName() {
        return lastName;
    }

    @Transient
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @Column(name = "language")
    @JsonAlias("language_code")
    public String getLanguage() {
        return language;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
