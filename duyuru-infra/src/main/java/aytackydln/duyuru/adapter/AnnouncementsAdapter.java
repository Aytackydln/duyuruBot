package aytackydln.duyuru.adapter;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.announcement.port.AnnouncementPort;
import aytackydln.duyuru.jpa.repository.AnnouncementRepository;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.jpa.entity.AnnouncementEntity;
import aytackydln.duyuru.jpa.entity.TopicEntity;
import aytackydln.duyuru.mapper.AnnouncementMapper;
import aytackydln.duyuru.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnouncementsAdapter implements AnnouncementPort {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;
    private final TopicMapper topicMapper;

    @Override
    public boolean exists(String topic, String title, String link) {
        return announcementRepository.existsById(new AnnouncementEntity.Key(topic, title, link));
    }

    @Override
    public Announcement update(Announcement announcement) {
        AnnouncementEntity entity = announcementMapper.map(announcement);
        AnnouncementEntity savedEntity = announcementRepository.save(entity);
        return announcementMapper.map(savedEntity);
    }

    @Override
    public List<Announcement> retrieveAllByTopic(Topic topic) {
        TopicEntity topicEntity = topicMapper.map(topic);
        List<AnnouncementEntity> announcements = announcementRepository.getAllByTopic(topicEntity);
        return announcementMapper.map(announcements);
    }

    @Override
    public void removeAll(List<Announcement> persistedAnnouncementEntities) {

    }

}
