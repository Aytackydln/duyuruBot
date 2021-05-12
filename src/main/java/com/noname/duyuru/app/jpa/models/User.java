package com.noname.duyuru.app.jpa.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NamedEntityGraph(
        name = "user-subscriptions",
        attributeNodes = {
                @NamedAttributeNode("subscriptions")
        }
)
public class User implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String language;
    private String status;
    private List<Subscription> subscriptions;

    @Id
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
    public String getLastName() {
        return lastName;
    }

    @Transient
    @ToString.Include
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @JsonProperty("username")
    @ToString.Include
    public String getUsername() {
        return username;
    }

    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
