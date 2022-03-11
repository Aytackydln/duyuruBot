package aytackydln.duyuru.mapper;

import aytackydln.duyuru.jpa.entity.SubscriptionEntity;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.subscriber.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        config = DuyuruMapperConfig.class,
        uses = {TopicMapper.class}
)
public interface SubscriptionMapper {
    Subscription mapFromEntity(SubscriptionEntity subscriptionEntity);
    List<Subscription> mapFromEntity(Iterable<SubscriptionEntity> subscriptions);

    SubscriptionEntity map(Subscription subscription);
    List<SubscriptionEntity> map(Iterable<Subscription> subscriptions);
}
