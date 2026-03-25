package com.salesusers.usermanagement.shared.exception;

import com.salesusers.usermanagement.domain.exception.DuplicateUserEmailException;
import com.salesusers.usermanagement.infrastructure.api.generated.model.ErrorResponse;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(this::buildFieldErrorMessage)
            .orElse("Request validation failed.");

        return ResponseEntity.badRequest().body(errorResponse("bad_request", message));
    }

    @ExceptionHandler(DuplicateUserEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserEmailException(DuplicateUserEmailException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorResponse("user_already_exists", exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(errorResponse("bad_request", "Request body is missing or malformed."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse("internal_error", "An unexpected error occurred."));
    }

    private String buildFieldErrorMessage(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage() == null ? "is invalid" : fieldError.getDefaultMessage();
        return "Field '%s' %s".formatted(fieldError.getField(), defaultMessage);
    }

    private ErrorResponse errorResponse(String code, String message) {
        return new ErrorResponse()
            .code(code)
            .message(message)
            .timestamp(OffsetDateTime.now(clock));
    }
}
