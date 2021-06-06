package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u left join fetch u.subscriptions",
            countQuery = "select count(u) from User u")
    Page<User> getUsersWithSubscriptions(Pageable pageable);

    @Query("select u from User u left join fetch u.subscriptions where u.id = :userId")
    User getUserWithSubscriptions(long userId);
}
