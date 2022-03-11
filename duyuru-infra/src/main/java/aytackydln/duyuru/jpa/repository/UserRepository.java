package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select u from User u left join fetch u.subscriptions",
            countQuery = "select count(u) from User u")
    Page<UserEntity> getUserWithSubscriptions(Pageable pageable);

    @Query("select u from User u left join fetch u.subscriptions where u.id = :userId")
    Optional<UserEntity> getUserWithSubscriptions(@Param("userId") long userId);
}
