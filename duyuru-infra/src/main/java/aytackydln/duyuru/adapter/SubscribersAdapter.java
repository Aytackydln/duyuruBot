package aytackydln.duyuru.adapter;

import aytackydln.duyuru.common.ModelPage;
import aytackydln.duyuru.common.PageDetails;
import aytackydln.duyuru.jpa.entity.UserEntity;
import aytackydln.duyuru.jpa.repository.UserRepository;
import aytackydln.duyuru.mapper.PageMapper;
import aytackydln.duyuru.mapper.UserMapper;
import aytackydln.duyuru.subscriber.Subscriber;
import aytackydln.duyuru.subscriber.port.SubscriberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscribersAdapter implements SubscriberPort {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;

    @Override
    public Subscriber getUserFetchSubscriptions(long id) {
        UserEntity userWithSubscriptions = userRepository.getUserWithSubscriptions(id).orElseThrow();
        return userMapper.mapFromEntity(userWithSubscriptions);
    }

    @Override
    public ModelPage<Subscriber> getUserFetchSubscriptions(PageDetails pageDetails) {
        PageRequest pageRequest = pageMapper.map(pageDetails);

        Page<UserEntity> userEntityModelPage = userRepository.getUserWithSubscriptions(pageRequest);
        PageDetails responsePageDetails = pageMapper.map(userEntityModelPage.getPageable(), userEntityModelPage.getTotalElements());

        List<UserEntity> content = userEntityModelPage.getContent();
        List<Subscriber> subscribers = userMapper.mapFromEntity(content);

        return new ModelPage<>(subscribers, responsePageDetails);
    }

    @Override
    public Optional<Subscriber> findById(long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userMapper::mapFromEntity);
    }
}
