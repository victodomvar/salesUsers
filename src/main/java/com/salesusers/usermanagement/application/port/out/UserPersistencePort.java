package com.salesusers.usermanagement.application.port.out;

import com.salesusers.usermanagement.domain.model.User;

public interface UserPersistencePort {

    boolean existsByEmail(String email);

    User save(User user);
}
