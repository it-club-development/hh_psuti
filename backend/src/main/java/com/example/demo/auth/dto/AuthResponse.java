package com.example.demo.auth.dto;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String role;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String email) {
        this.token = token;
        this.email = email;
        this.message = "Login successful";
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}