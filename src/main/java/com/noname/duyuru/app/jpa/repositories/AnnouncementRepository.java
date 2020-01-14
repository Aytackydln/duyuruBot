package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Announcement;
import com.noname.duyuru.app.jpa.models.AnnouncementKey;
import com.noname.duyuru.app.jpa.models.Topic;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, AnnouncementKey> {
    List<Announcement> getAllByTopic(Topic topic);
}
