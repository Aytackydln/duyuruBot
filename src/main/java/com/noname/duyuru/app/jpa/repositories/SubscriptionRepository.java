package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Subscription;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Subscription.Key> {
    List<Subscription> findAllByTopic(Topic topic);

    @Query("select distinct s.topic from Subscription s")
    List<Topic> findDistinctTopics();

    @Query("select s.topic from Subscription s where s.user = :user")
    List<Topic> findUserTopics(User user);
}
