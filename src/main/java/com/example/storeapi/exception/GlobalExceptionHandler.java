package com.example.storeapi.exception;

import com.example.storeapi.exception.auth.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Internal server error: ", ex);  // log full stack trace
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<LoginErrorResponse> handleLoginException(LoginException ex) {
        return new ResponseEntity<LoginErrorResponse>(ex.getError(), ex.getStatus());
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<RegisterErrorResponse> handleRegisterException(RegisterException ex) {
        return new ResponseEntity<RegisterErrorResponse>(ex.getError(), ex.getStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthErrorResponse> handleAuthException(AuthException ex) {
        return new ResponseEntity<AuthErrorResponse>(ex.getError(), ex.getStatus());
    }
}