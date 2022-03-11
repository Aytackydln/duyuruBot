package aytackydln.duyuru;

import aytackydln.duyuru.subscriber.SubscriberFacade;
import lombok.Value;

/**
 * Used for user exeptions
 */
@Value
public class ServiceContext {
    SubscriberFacade chatUserService;
}
