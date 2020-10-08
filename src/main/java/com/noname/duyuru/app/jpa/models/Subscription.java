package com.noname.duyuru.app.jpa.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(SubscriptionKey.class)
public class Subscription implements Serializable {
    private final SubscriptionKey id = new SubscriptionKey();
    private User user;
    private Topic topic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                '}';
    }

    @Transient
    public SubscriptionKey getId() {
        return id;
    }

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @ManyToOne(optional = false)
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
