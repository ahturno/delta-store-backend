package com.example.storeapi.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginException extends RuntimeException {

    private final HttpStatus status;
    private final LoginErrorResponse error;

    public LoginException(HttpStatus status, LoginErrorResponse error) {
        super(error.getDefaultError());
        this.status = status;
        this.error = error;
    }

}