package org.example.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private final String token;
    private final Boolean success;

    public AuthResponse(String token, Boolean success) {
        this.token = token;
        this.success = success;
    }
}
