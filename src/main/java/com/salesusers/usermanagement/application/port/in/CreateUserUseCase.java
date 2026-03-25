package com.salesusers.usermanagement.application.port.in;

public interface CreateUserUseCase {

    UserResult createUser(CreateUserCommand command);
}
