package com.salesusers.usermanagement.infrastructure.persistence.mapper;

import com.salesusers.usermanagement.domain.model.User;
import com.salesusers.usermanagement.infrastructure.persistence.jpa.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(user.id(), user.name(), user.email(), user.createdAt());
    }

    public User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getCreatedAt());
    }
}
