package com.salesusers.usermanagement.controller;

import com.salesusers.usermanagement.dto.request.CreateUserRequestDto;
import com.salesusers.usermanagement.dto.response.UserResponseDto;
import com.salesusers.usermanagement.infrastructure.api.generated.UsersApi;
import com.salesusers.usermanagement.infrastructure.api.generated.model.CreateUserRequest;
import com.salesusers.usermanagement.infrastructure.api.generated.model.UserResponse;
import com.salesusers.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(@Valid CreateUserRequest request) {
        UserResponseDto response = userService.createUser(new CreateUserRequestDto(
            request.getName(),
            request.getEmail()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new UserResponse()
                .id(response.id())
                .name(response.name())
                .email(response.email())
                .createdAt(response.createdAt()));
    }
}
