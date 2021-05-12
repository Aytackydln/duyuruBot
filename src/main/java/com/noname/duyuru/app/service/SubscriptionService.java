package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final AnnouncementService announcementService;

    public void checkNewAnnouncements() {
        final List<Topic> topics = subscriptionRepository.findDistinctTopics();
        for (final Topic topic : topics) {
            LOGGER.debug("Checking {}", topic.getId());
            announcementService.checkAnnouncements(topic);
        }
    }
}
