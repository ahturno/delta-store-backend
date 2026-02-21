package com.example.storeapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthResponse {
    private UUID id;
    private String email;
    private String fullName;

    public AuthResponse(UUID id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }
}
