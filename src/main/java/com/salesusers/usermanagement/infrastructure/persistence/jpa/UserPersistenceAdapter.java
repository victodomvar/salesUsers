package com.salesusers.usermanagement.infrastructure.persistence.jpa;

import com.salesusers.usermanagement.application.port.out.UserPersistencePort;
import com.salesusers.usermanagement.domain.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.domain.model.User;
import com.salesusers.usermanagement.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper userPersistenceMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        try {
            UserEntity savedEntity = userJpaRepository.save(userPersistenceMapper.toEntity(user));
            return userPersistenceMapper.toDomain(savedEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateUserEmailException(user.email());
        }
    }
}
