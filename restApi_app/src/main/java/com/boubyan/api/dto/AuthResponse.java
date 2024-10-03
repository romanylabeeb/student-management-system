package com.boubyan.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String username;
    private String role;

    public AuthResponse(String username, String role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }
}
