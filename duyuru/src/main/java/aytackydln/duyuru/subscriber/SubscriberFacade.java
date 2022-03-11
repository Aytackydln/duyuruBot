package aytackydln.duyuru.subscriber;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.chatplatform.port.TelegramPort;
import aytackydln.duyuru.common.ModelPage;
import aytackydln.duyuru.common.PageDetails;
import aytackydln.duyuru.common.semantic.DomainComponent;
import aytackydln.duyuru.subscriber.port.SubscriberPort;
import aytackydln.duyuru.subscriber.port.SubscriptionPort;
import aytackydln.duyuru.topic.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DomainComponent
public class SubscriberFacade {
    private final SubscriptionPort subscriptionPort;
    private final SubscriberPort subscriberPort;
    private final TelegramPort telegramPort;

    public void notifySubscribers(Announcement announcement) {
        for (Subscriber subscriber : subscriptionPort.getSubscribers(announcement.getTopic())) {
            telegramPort.sendMessage(subscriber, announcement.toString());
        }
    }

    public void setUserStatusDisabled(long id) {
        var user = subscriberPort.getUserFetchSubscriptions(id);
        LOGGER.info("Setting {} status \"disabled\"", user);
        user.setStatus("disabled");
        subscriptionPort.deleteAll(user.getSubscriptions());
        user.getSubscriptions().clear();
    }

    public ModelPage<Subscriber> getUsersWithSubscriptions(PageDetails pageDetails) {
        return subscriberPort.getUserFetchSubscriptions(pageDetails);
    }

    public void clearUserStatus(long id) {
        var user = subscriberPort.findById(id).orElseThrow();
        LOGGER.info("Setting {} status \"enabled\"", user);
        user.setStatus(null);
    }

    public List<Topic> findUserTopics(Subscriber subscriber) {
        return subscriptionPort.getSubscriptions(subscriber);
    }
}
