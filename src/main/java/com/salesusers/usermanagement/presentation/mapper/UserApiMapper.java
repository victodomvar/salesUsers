package com.salesusers.usermanagement.presentation.mapper;

import com.salesusers.usermanagement.application.port.in.CreateUserCommand;
import com.salesusers.usermanagement.application.port.in.UserResult;
import com.salesusers.usermanagement.infrastructure.api.generated.model.CreateUserRequest;
import com.salesusers.usermanagement.infrastructure.api.generated.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserApiMapper {

    public CreateUserCommand toCommand(CreateUserRequest request) {
        return new CreateUserCommand(request.getName(), request.getEmail());
    }

    public UserResponse toResponse(UserResult result) {
        return new UserResponse()
            .id(result.id())
            .name(result.name())
            .email(result.email())
            .createdAt(result.createdAt());
    }
}
