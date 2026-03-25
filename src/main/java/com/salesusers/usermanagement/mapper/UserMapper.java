package com.salesusers.usermanagement.mapper;

import com.salesusers.usermanagement.dto.request.CreateUserRequestDto;
import com.salesusers.usermanagement.dto.response.UserResponseDto;
import com.salesusers.usermanagement.infrastructure.api.generated.model.CreateUserRequest;
import com.salesusers.usermanagement.infrastructure.api.generated.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public CreateUserRequestDto toRequestDto(CreateUserRequest request) {
        return new CreateUserRequestDto(request.getName(), request.getEmail());
    }

    public UserResponse toApiResponse(UserResponseDto responseDto) {
        return new UserResponse()
            .id(responseDto.id())
            .name(responseDto.name())
            .email(responseDto.email())
            .createdAt(responseDto.createdAt());
    }
}
