package aytackydln.duyuru.announcement;

import aytackydln.duyuru.announcement.port.AnnouncementPort;
import aytackydln.duyuru.announcement.port.AnnouncementReaderPort;
import aytackydln.duyuru.common.semantic.Action;
import aytackydln.duyuru.common.semantic.DomainComponent;
import aytackydln.duyuru.subscriber.SubscriberFacade;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.topic.port.TopicPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@DomainComponent
public class AnnouncementFacade {
    private final AnnouncementPort announcementPort;
    private final AnnouncementReaderPort announcementReaderPort;
    private final TopicPort topicPort;
    private final SubscriberFacade subscriberFacade;

    @Action
    public List<String> clearAnnouncements() {
        List<Announcement> clearedAnnouncementEntities = new ArrayList<>();
        for (Topic topic : topicPort.getAllNonDepartment()) {
            clearedAnnouncementEntities.addAll(clearAnnouncements(topic));
        }
        Stream<String> clearedAnnouncementStrings = clearedAnnouncementEntities.stream().map(Announcement::toString);
        LOGGER.info("Cleared announcements; {}", clearedAnnouncementStrings);
        return clearedAnnouncementStrings.toList();
    }

    @Action
    public void checkNewAnnouncements() {
        final List<Topic> topicEntities = topicPort.getSubscribedTopics();
        for (final Topic topic : topicEntities) {
            LOGGER.debug("Checking {}", topic.getId());
            checkNewAnnouncements(topic);
        }
    }

    private void checkNewAnnouncements(Topic topic){
        List<Announcement> announcements = readTopicAnnouncements(topic);
        for (var announcement : announcements) {
            try {
                announcement.setDate(new Date());

                if (!announcementPort.exists(announcement.getTopic().getId(), announcement.getTitle(), announcement.getLink())) {
                    announcementPort.update(announcement);
                    subscriberFacade.notifySubscribers(announcement);
                }
            } catch (Exception e) {
                LOGGER.error("Error while checking announcement", e);
            }
        }
    }

    private List<Announcement> clearAnnouncements(Topic topic) {
        List<Announcement> persistedAnnouncementEntities = announcementPort.retrieveAllByTopic(topic);
        List<Announcement> currentAnnouncementEntities = readTopicAnnouncements(topic);
        persistedAnnouncementEntities.removeAll(currentAnnouncementEntities);

        announcementPort.removeAll(persistedAnnouncementEntities);
        return persistedAnnouncementEntities;
    }

    private List<Announcement> readTopicAnnouncements(Topic topic){
        return announcementReaderPort.readAnnouncements(topic);
    }
}
