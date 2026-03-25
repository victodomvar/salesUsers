package com.salesusers.usermanagement.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record User(UUID id, String name, String email, OffsetDateTime createdAt) {
}
