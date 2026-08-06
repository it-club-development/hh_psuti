package com.example.demo.auth.dto;

//ответ

public class AuthResponse {
    private String token;
    private String email;
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

    // приём и отдача
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
