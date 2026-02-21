package com.example.storeapi.exception.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterErrorResponse {
    private final String defaultError;
    private final String email;
    private final String fullName;
    private final String password;
}
