package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Subscription;
import com.noname.duyuru.app.jpa.models.SubscriptionKey;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionKey> {
    List<Subscription> findAllByTopic(Topic topic);

    @Query("select distinct s.topic from Subscription s")
    List<Topic> findDistinctTopics();

    @Query("select distinct s.user from Subscription s")
    Page<User> getUsersWithSubscriptions(Pageable pageable);

    @Query("select s.topic from Subscription s where s.user = :user")
    List<Topic> findUserTopics(User user);
}
