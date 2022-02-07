package aytackydln.duyuru.jpa.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Subscription")
@IdClass(SubscriptionEntity.Key.class)
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SubscriptionEntity implements Serializable {
    private final SubscriptionEntity.Key id = new SubscriptionEntity.Key();
    private UserEntity user;
    private TopicEntity topic;

    @Transient
    @EqualsAndHashCode.Include
    @ToString.Include
    public SubscriptionEntity.Key getId() {
        return id;
    }

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
        if (userEntity != null)
            this.id.setUser(userEntity.getId());
        else
            this.id.setUser(0);
    }

    @Id
    @ManyToOne(optional = false)
    public TopicEntity getTopic() {
        return topic;
    }

    public void setTopic(TopicEntity topicEntity) {
        this.topic = topicEntity;
        if (topicEntity != null)
            this.id.setTopic(topicEntity.getId());
        else
            this.id.setTopic(null);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode
    public static class Key implements Serializable {
        private long user;
        private String topic;
    }
}
