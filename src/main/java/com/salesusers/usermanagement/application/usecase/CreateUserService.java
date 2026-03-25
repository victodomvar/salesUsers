package com.salesusers.usermanagement.application.usecase;

import com.salesusers.usermanagement.application.port.in.CreateUserCommand;
import com.salesusers.usermanagement.application.port.in.CreateUserUseCase;
import com.salesusers.usermanagement.application.port.in.UserResult;
import com.salesusers.usermanagement.application.port.out.UserPersistencePort;
import com.salesusers.usermanagement.domain.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.domain.model.User;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CreateUserService implements CreateUserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final Clock clock;

    public CreateUserService(UserPersistencePort userPersistencePort, Clock clock) {
        this.userPersistencePort = userPersistencePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public UserResult createUser(CreateUserCommand command) {
        String normalizedName = command.name().trim();
        String normalizedEmail = command.email().trim().toLowerCase(Locale.ROOT);

        if (userPersistencePort.existsByEmail(normalizedEmail)) {
            throw new DuplicateUserEmailException(normalizedEmail);
        }

        User createdUser = userPersistencePort.save(new User(
            UUID.randomUUID(),
            normalizedName,
            normalizedEmail,
            OffsetDateTime.now(clock)
        ));

        return new UserResult(
            createdUser.id(),
            createdUser.name(),
            createdUser.email(),
            createdUser.createdAt()
        );
    }
}
