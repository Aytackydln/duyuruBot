package aytackydln.duyuru.announcement.port;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.topic.Topic;

import java.util.List;

public interface AnnouncementReaderPort {
    List<Announcement> readAnnouncements(Topic topic);
}
