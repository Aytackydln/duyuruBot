package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.models.MessageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<MessageEntity,Long> {
}
