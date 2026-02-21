package com.example.storeapi.exception.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginErrorResponse {
    private final String defaultError;
    private final String email;
    private final String password;
}