package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.TopicEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends PagingAndSortingRepository<TopicEntity, String> {
    List<TopicEntity> findAll();

    List<TopicEntity> getByDepartmentId(String string);

    List<TopicEntity> getByDepartmentIdNotNull();

    boolean existsByBoardAppend(String boardAppend);
}
