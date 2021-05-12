package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> getUsersWithSubscriptions(Pageable pageable) {
        return userRepository.getUsersWithSubscriptions(pageable);
    }
}
