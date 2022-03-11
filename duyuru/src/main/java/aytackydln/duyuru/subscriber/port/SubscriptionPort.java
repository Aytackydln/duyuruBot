package aytackydln.duyuru.subscriber.port;

import aytackydln.duyuru.subscriber.Subscriber;
import aytackydln.duyuru.subscriber.Subscription;
import aytackydln.duyuru.topic.Topic;

import java.util.List;

public interface SubscriptionPort {
    Iterable<Subscriber> getSubscribers(Topic topic);

    void deleteAll(List<Subscription> subscriptions);

    void update(Subscription subscription);
    void remove(Subscription subscription);

    List<Topic> getSubscriptions(Subscriber subscriber);
}
