package com.example.demo.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String role;
    private String message;
}