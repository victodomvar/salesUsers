package com.salesusers.usermanagement.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record User(UUID id, String name, String email, OffsetDateTime createdAt) {
}
