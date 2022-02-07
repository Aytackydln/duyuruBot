package aytackydln.duyuru.service;

import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
import aytackydln.duyuru.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatUserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public Page<UserEntity> getUsersWithSubscriptions(Pageable pageable) {
        return userRepository.getUserWithSubscriptions(pageable);
    }

    @Transactional
    public void setUserStatusDisabled(long id) {
        var user = userRepository.getUserWithSubscriptions(id);
        LOGGER.info("Setting {} status \"disabled\"", user);
        user.setStatus("disabled");
        subscriptionRepository.deleteAll(user.getSubscriptions());
        user.getSubscriptions().clear();
    }

    @Transactional
    public void clearUserStatus(long id) {
        var user = userRepository.findById(id).orElseThrow();
        LOGGER.info("Setting {} status \"enabled\"", user);
        user.setStatus(null);
    }
}
