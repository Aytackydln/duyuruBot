package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.TopicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends PagingAndSortingRepository<Topic, String> {
	List<Topic> getByType(TopicType topicType);
	List<Topic> findAll();
}
