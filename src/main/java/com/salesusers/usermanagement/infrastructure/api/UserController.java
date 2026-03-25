package com.salesusers.usermanagement.infrastructure.api;

import com.salesusers.usermanagement.application.port.in.CreateUserUseCase;
import com.salesusers.usermanagement.application.port.in.UserResult;
import com.salesusers.usermanagement.infrastructure.api.generated.UsersApi;
import com.salesusers.usermanagement.infrastructure.api.generated.model.CreateUserRequest;
import com.salesusers.usermanagement.infrastructure.api.generated.model.UserResponse;
import com.salesusers.usermanagement.infrastructure.api.mapper.UserApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    private final CreateUserUseCase createUserUseCase;
    private final UserApiMapper userApiMapper;

    public UserController(CreateUserUseCase createUserUseCase, UserApiMapper userApiMapper) {
        this.createUserUseCase = createUserUseCase;
        this.userApiMapper = userApiMapper;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(@Valid CreateUserRequest request) {
        UserResult result = createUserUseCase.createUser(userApiMapper.toCommand(request));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userApiMapper.toResponse(result));
    }
}
