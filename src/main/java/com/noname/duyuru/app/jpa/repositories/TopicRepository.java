package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Topic;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends PagingAndSortingRepository<Topic, String> {
    List<Topic> findAll();

    List<Topic> getByDepartmentId(String string);

    List<Topic> getByDepartmentIdNotNull();

    boolean existsByBoardAppend(String boardAppend);
}
