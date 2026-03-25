package com.salesusers.usermanagement.controller;

import com.salesusers.usermanagement.dto.request.CreateUserRequestDto;
import com.salesusers.usermanagement.dto.response.UserResponseDto;
import com.salesusers.usermanagement.infrastructure.api.generated.UsersApi;
import com.salesusers.usermanagement.infrastructure.api.generated.model.CreateUserRequest;
import com.salesusers.usermanagement.infrastructure.api.generated.model.UserResponse;
import com.salesusers.usermanagement.mapper.UserMapper;
import com.salesusers.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(@Valid CreateUserRequest createUserRequest) {
        CreateUserRequestDto requestDto = userMapper.toRequestDto(createUserRequest);
        UserResponseDto responseDto = userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userMapper.toApiResponse(responseDto));
    }
}
