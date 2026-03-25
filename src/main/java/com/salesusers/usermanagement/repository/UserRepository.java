package com.salesusers.usermanagement.repository;

import com.salesusers.usermanagement.domain.User;

public interface UserRepository {

    User save(User user);
}
