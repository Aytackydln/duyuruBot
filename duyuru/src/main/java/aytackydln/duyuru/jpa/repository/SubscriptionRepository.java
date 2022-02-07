package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.models.SubscriptionEntity;
import aytackydln.duyuru.jpa.models.TopicEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, SubscriptionEntity.Key> {
    List<SubscriptionEntity> findAllByTopic(TopicEntity topicEntity);

    @Query("select distinct s.topic from Subscription s")
    List<TopicEntity> findDistinctTopics();

    @Query("select s.topic from Subscription s where s.user = :userEntity")
    List<TopicEntity> findUserTopics(@Param("userEntity") UserEntity userEntity);
}
