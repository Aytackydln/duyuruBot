package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.AnnouncementEntity;
import aytackydln.duyuru.jpa.entity.TopicEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends PagingAndSortingRepository<AnnouncementEntity, AnnouncementEntity.Key> {
    List<AnnouncementEntity> getAllByTopic(TopicEntity topicEntity);
}
