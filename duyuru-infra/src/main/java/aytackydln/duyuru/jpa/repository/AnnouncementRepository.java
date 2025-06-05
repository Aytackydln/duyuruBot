package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.AnnouncementEntity;
import aytackydln.duyuru.jpa.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, AnnouncementEntity.Key> {
    List<AnnouncementEntity> getAllByTopic(TopicEntity topicEntity);
}
