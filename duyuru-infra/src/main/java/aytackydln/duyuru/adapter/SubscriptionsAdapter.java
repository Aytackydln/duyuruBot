package aytackydln.duyuru.adapter;

import aytackydln.duyuru.jpa.entity.SubscriptionEntity;
import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
import aytackydln.duyuru.subscriber.Subscriber;
import aytackydln.duyuru.subscriber.Subscription;
import aytackydln.duyuru.subscriber.port.SubscriptionPort;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.jpa.entity.TopicEntity;
import aytackydln.duyuru.jpa.entity.UserEntity;
import aytackydln.duyuru.mapper.SubscriptionMapper;
import aytackydln.duyuru.mapper.TopicMapper;
import aytackydln.duyuru.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionsAdapter implements SubscriptionPort {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final TopicMapper topicMapper;
    private final UserMapper userMapper;

    @Override
    public Iterable<Subscriber> getSubscribers(Topic topic) {
        TopicEntity entity = topicMapper.map(topic);
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findAllByTopic(entity);
        return subscriptions.stream().map(subscriptionEntity -> {
            UserEntity user = subscriptionEntity.getUser();
            return userMapper.mapFromEntity(user);
        }).toList();
    }

    @Override
    public void deleteAll(List<Subscription> subscriptions) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionMapper.map(subscriptions);
        subscriptionRepository.deleteAll(subscriptionEntities);
    }

    @Override
    public void update(Subscription subscription) {
        SubscriptionEntity entity = subscriptionMapper.map(subscription);
        subscriptionRepository.save(entity);
    }

    @Override
    public void remove(Subscription subscription) {
        SubscriptionEntity entity = subscriptionMapper.map(subscription);
        subscriptionRepository.delete(entity);
    }

    @Override
    public List<Topic> getSubscriptions(Subscriber subscriber) {
        UserEntity userEntity = userMapper.map(subscriber);
        List<TopicEntity> userTopics = subscriptionRepository.findUserTopics(userEntity);
        return topicMapper.mapFromEntity(userTopics);
    }
}
