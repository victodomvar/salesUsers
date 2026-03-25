package com.salesusers.usermanagement.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email, OffsetDateTime createdAt) {
}
