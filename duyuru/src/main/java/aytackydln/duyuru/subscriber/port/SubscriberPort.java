package aytackydln.duyuru.subscriber.port;

import aytackydln.duyuru.common.ModelPage;
import aytackydln.duyuru.common.PageDetails;
import aytackydln.duyuru.subscriber.Subscriber;

import java.util.Optional;

public interface SubscriberPort {
    Subscriber getUserFetchSubscriptions(long id);

    ModelPage<Subscriber> getUserFetchSubscriptions(PageDetails pageDetails);

    Optional<Subscriber> findById(long id);
}
