package com.salesusers.usermanagement.repository;

import com.salesusers.usermanagement.domain.User;
import com.salesusers.usermanagement.exception.DuplicateUserEmailException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        User existingUser = usersByEmail.putIfAbsent(user.email(), user);
        if (existingUser != null) {
            throw new DuplicateUserEmailException(user.email());
        }

        return user;
    }
}
