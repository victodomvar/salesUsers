package com.salesusers.usermanagement.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salesusers.usermanagement.application.port.in.CreateUserCommand;
import com.salesusers.usermanagement.application.port.in.UserResult;
import com.salesusers.usermanagement.application.port.out.UserPersistencePort;
import com.salesusers.usermanagement.domain.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2026-03-25T12:00:00Z");

    @Mock
    private UserPersistencePort userPersistencePort;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        createUserService = new CreateUserService(
            userPersistencePort,
            Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC)
        );
    }

    @Test
    void shouldNormalizeInputAndReturnCreatedUser() {
        when(userPersistencePort.existsByEmail("alice@example.com")).thenReturn(false);
        when(userPersistencePort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResult result = createUserService.createUser(
            new CreateUserCommand("  Alice Doe  ", "  Alice@Example.COM  ")
        );

        verify(userPersistencePort).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.id());
        assertEquals("Alice Doe", savedUser.name());
        assertEquals("alice@example.com", savedUser.email());
        assertEquals(OffsetDateTime.ofInstant(FIXED_INSTANT, ZoneOffset.UTC), savedUser.createdAt());

        assertEquals(savedUser.id(), result.id());
        assertEquals("Alice Doe", result.name());
        assertEquals("alice@example.com", result.email());
        assertEquals(OffsetDateTime.ofInstant(FIXED_INSTANT, ZoneOffset.UTC), result.createdAt());
    }

    @Test
    void shouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists() {
        when(userPersistencePort.existsByEmail("alice@example.com")).thenReturn(true);

        DuplicateUserEmailException thrown = assertThrows(
            DuplicateUserEmailException.class,
            () -> createUserService.createUser(new CreateUserCommand("Alice Doe", "alice@example.com"))
        );

        assertEquals("A user with email 'alice@example.com' already exists.", thrown.getMessage());
        verify(userPersistencePort, never()).save(any(User.class));
    }
}
