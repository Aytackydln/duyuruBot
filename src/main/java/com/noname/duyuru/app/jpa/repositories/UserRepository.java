package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "select u from User u join fetch u.subscriptions where size(u.subscriptions) > 0",
            countQuery = "select count(u) from User u where size(u.subscriptions) > 0")
    Page<User> getUsersWithSubscriptions(Pageable pageable);
}
