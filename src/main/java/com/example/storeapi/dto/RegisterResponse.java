package com.example.storeapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String token;

    public RegisterResponse(UUID id, String email, String fullName, String token) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
    }

    // getters and setters
}