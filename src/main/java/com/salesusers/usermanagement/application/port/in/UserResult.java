package com.salesusers.usermanagement.application.port.in;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResult(UUID id, String name, String email, OffsetDateTime createdAt) {
}
