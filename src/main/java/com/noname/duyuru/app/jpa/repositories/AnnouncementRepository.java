package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Announcement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, AnnouncementKey> {
}
