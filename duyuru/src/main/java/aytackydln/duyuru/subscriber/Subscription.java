package aytackydln.duyuru.subscriber;

import aytackydln.duyuru.topic.Topic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription {
    private Subscriber user;
    private Topic topic;
}
