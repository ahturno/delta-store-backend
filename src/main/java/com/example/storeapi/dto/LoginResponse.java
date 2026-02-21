package com.example.storeapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginResponse {

    private UUID id;
    private String email;
    private String fullName;

    public LoginResponse(UUID id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    // getters and setters
}