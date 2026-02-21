package com.example.storeapi.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RegisterException extends RuntimeException {
    private final HttpStatus status;
    private final RegisterErrorResponse error;

    public RegisterException(HttpStatus status, RegisterErrorResponse error) {
        super(error.getDefaultError());
        this.status = status;
        this.error = error;
    }
}
