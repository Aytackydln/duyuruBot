package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import com.noname.duyuru.app.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public Page<User> getUsersWithSubscriptions(Pageable pageable) {
        return userRepository.getUsersWithSubscriptions(pageable);
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
        var user = userRepository.getOne(id);
        LOGGER.info("Setting {} status \"enabled\"", user);
        user.setStatus(null);
    }
}
