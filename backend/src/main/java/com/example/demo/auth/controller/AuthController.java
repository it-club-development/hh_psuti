package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.user;
import com.example.demo.auth.security.IpUtil;
import com.example.demo.auth.security.JWT_util;
import com.example.demo.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWT_util jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IpUtil ipUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest loginRequest,
                                   HttpServletRequest request) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String ipAddress = ipUtil.getClientIp(request);
        System.out.println("🔐 Попытка входа с IP: " + ipAddress);

        user user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Пользователь не найден"
            ));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Неверный пароль"
            ));
        }

        if (!user.isTermsAccepted()) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Необходимо принять пользовательское соглашение",
                    "needAcceptTerms", true,
                    "userId", user.getId()
            ));
        }

        userService.updateLastLogin(email, ipAddress);
        String token = jwtUtil.generateToken(email);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(email);
        response.setRole(user.getRole() != null ? user.getRole().name() : "STUDENT");
        response.setMessage("Успешный вход");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
                                      HttpServletRequest request) {

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Пароли не совпадают"
            ));
        }

        String ipAddress = ipUtil.getClientIp(request);
        System.out.println("📝 Попытка регистрации с IP: " + ipAddress);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email не может быть пустым"
            ));
        }

        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Пароль не может быть пустым"
            ));
        }

        if (password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Пароль должен содержать минимум 6 символов"
            ));
        }

        if (userService.userExists(email)) {
            return ResponseEntity.status(409).body(Map.of(
                    "error", "Пользователь с таким email уже существует"
            ));
        }

        user newUser = new user();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(Role.STUDENT);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(true);
        newUser.setTermsAccepted(false);
        newUser.setIpAddress(ipAddress);

        boolean registered = userService.registerUser(newUser);

        if (registered) {
            String token = jwtUtil.generateToken(email);
            System.out.println("✅ Успешная регистрация: " + email + " (IP: " + ipAddress + ")");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("role", "STUDENT");
            response.put("message", "Регистрация успешна. Пожалуйста, примите пользовательское соглашение.");
            response.put("needAcceptTerms", true);
            response.put("userId", userService.getUserByEmail(email).getId());

            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ошибка при регистрации пользователя"
            ));
        }
    }

    @PostMapping("/accept-terms")
    public ResponseEntity<?> acceptTerms(@RequestParam Long userId) {
        boolean accepted = userService.acceptTerms(userId);
        if (accepted) {
            return ResponseEntity.ok(Map.of(
                    "message", "Пользовательское соглашение принято",
                    "termsAccepted", true
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Пользователь не найден"
            ));
        }
    }

    @GetMapping("/check-terms/{userId}")
    public ResponseEntity<?> checkTerms(@PathVariable Long userId) {
        boolean accepted = userService.isTermsAccepted(userId);
        return ResponseEntity.ok(Map.of(
                "termsAccepted", accepted
        ));
    }

    @GetMapping("/my-ip")
    public ResponseEntity<?> getMyIp(HttpServletRequest request) {
        String ip = ipUtil.getClientIp(request);
        return ResponseEntity.ok(Map.of(
                "ip", ip,
                "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/check/{email}")
    public ResponseEntity<?> checkUser(@PathVariable String email) {
        boolean exists = userService.userExists(email);
        return ResponseEntity.ok(Map.of(
                "email", email,
                "exists", exists
        ));
    }
}