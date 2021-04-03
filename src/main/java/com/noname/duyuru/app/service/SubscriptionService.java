package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final AnnouncementService announcementService;

    @Transactional(readOnly = true)
    public Page<User> getUsersWithSubscriptions(Pageable pageable) {
        final Page<User> usersWithSubscriptions = subscriptionRepository.getUsersWithSubscriptions(pageable);
        for (User u : usersWithSubscriptions) {
            u.getSubscriptions().size();
        }
        return usersWithSubscriptions;
    }

    public void checkNewAnnouncements() {
        final List<Topic> topics = subscriptionRepository.findDistinctTopics();
        for (final Topic topic : topics) {
            LOGGER.debug("Checking {}", topic.getId());
            try {
                announcementService.checkAnnouncements(topic);
            } catch (final ResourceAccessException | IOException e) {   //useless exception catch, method is async
                LOGGER.error("could not access topic: {}({})", topic.getId(), e.getClass().getName());
            }
        }
    }
}
