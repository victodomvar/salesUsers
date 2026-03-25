package com.salesusers.usermanagement.exception;

public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(String email) {
        super("A user with email '%s' already exists.".formatted(email));
    }
}
