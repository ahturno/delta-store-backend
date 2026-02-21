package com.example.storeapi.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {
    private final HttpStatus status;
    private final AuthErrorResponse error;

    public AuthException(HttpStatus status, AuthErrorResponse error) {
        super(error.getMessage());
        this.status = status;
        this.error = error;
    }
}
