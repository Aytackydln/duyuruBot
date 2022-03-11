package aytackydln.duyuru.announcement.port;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.topic.Topic;

import java.util.List;

public interface AnnouncementPort {
    boolean exists(String topic, String title, String link);
    Announcement update(Announcement announcement);
    List<Announcement> retrieveAllByTopic(Topic topic);
    void removeAll(List<Announcement> persistedAnnouncementEntities);
}
