package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, String> {

    List<TopicEntity> getByDepartmentId(String string);

    List<TopicEntity> getByDepartmentIdNotNull();

    boolean existsByBoardAppend(String boardAppend);
}
