package com.salesusers.usermanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salesusers.usermanagement.domain.User;
import com.salesusers.usermanagement.dto.request.CreateUserRequestDto;
import com.salesusers.usermanagement.dto.response.UserResponseDto;
import com.salesusers.usermanagement.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.repository.UserRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2026-03-25T12:00:00Z");

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserService userService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
        userService = new UserService(userRepository, fixedClock);
    }

    @Test
    void shouldNormalizeInputAndReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDto response = userService.createUser(
            new CreateUserRequestDto("  Alice Doe  ", "  Alice@Example.COM  ")
        );

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.id());
        assertEquals("Alice Doe", savedUser.name());
        assertEquals("alice@example.com", savedUser.email());
        assertEquals(OffsetDateTime.ofInstant(FIXED_INSTANT, ZoneOffset.UTC), savedUser.createdAt());

        assertEquals(savedUser.id(), response.id());
        assertEquals("Alice Doe", response.name());
        assertEquals("alice@example.com", response.email());
        assertEquals(OffsetDateTime.ofInstant(FIXED_INSTANT, ZoneOffset.UTC), response.createdAt());
    }

    @Test
    void shouldPropagateDuplicateEmailExceptionFromRepository() {
        DuplicateUserEmailException exception = new DuplicateUserEmailException("alice@example.com");
        when(userRepository.save(any(User.class))).thenThrow(exception);

        DuplicateUserEmailException thrown = assertThrows(
            DuplicateUserEmailException.class,
            () -> userService.createUser(new CreateUserRequestDto("Alice Doe", "alice@example.com"))
        );

        assertSame(exception, thrown);
    }
}
