package com.salesusers.usermanagement.service;

import com.salesusers.usermanagement.domain.User;
import com.salesusers.usermanagement.dto.request.CreateUserRequestDto;
import com.salesusers.usermanagement.dto.response.UserResponseDto;
import com.salesusers.usermanagement.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.repository.UserRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Clock clock;

    public UserService(UserRepository userRepository, Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        String normalizedName = request.name().trim();
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateUserEmailException(normalizedEmail);
        }

        User createdUser;
        try {
            createdUser = userRepository.save(new User(
                UUID.randomUUID(),
                normalizedName,
                normalizedEmail,
                OffsetDateTime.now(clock)
            ));
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateUserEmailException(normalizedEmail);
        }

        return new UserResponseDto(
            createdUser.getId(),
            createdUser.getName(),
            createdUser.getEmail(),
            createdUser.getCreatedAt()
        );
    }
}
