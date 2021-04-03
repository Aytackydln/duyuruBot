package com.noname.duyuru.app.jpa.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Subscription.Key.class)
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Subscription implements Serializable {
    private final Subscription.Key id = new Subscription.Key();
    private User user;
    private Topic topic;

    @Transient
    @EqualsAndHashCode.Include
    @ToString.Include
    public Subscription.Key getId() {
        return id;
    }

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    public User getUser() {
        return user;
    }

    @Id
    @ManyToOne(optional = false)
    public Topic getTopic() {
        return topic;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Key implements Serializable {
        private long user;
        private String topic;
    }
}
