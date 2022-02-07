package aytackydln.duyuru.service;

import aytackydln.duyuru.jpa.models.TopicEntity;
import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
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
        final List<TopicEntity> topicEntities = subscriptionRepository.findDistinctTopics();
        for (final TopicEntity topicEntity : topicEntities) {
            LOGGER.debug("Checking {}", topicEntity.getId());
            announcementService.checkAnnouncements(topicEntity);
        }
    }
}
