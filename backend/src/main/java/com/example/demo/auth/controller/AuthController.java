package com.example.demo.auth.controller;

import com.example.demo.General.Roles;
import com.example.demo.Models.Company_entity;
import com.example.demo.Models.Student_entity;
import com.example.demo.Models.User_entity;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.RegisterRequest;
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
import java.util.UUID;

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

        User_entity user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Пользователь не найден"
            ));
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Неверный пароль"
            ));
        }

        if (!user.isTermsAccepted()) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Необходимо принять пользовательское соглашение",
                    "needAcceptTerms", true,
                    "userId", user.getId().toString()
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
        String role = registerRequest.getRole();  // ← ДОБАВЛЕНО

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

        // Создаём пользователя в зависимости от роли
        User_entity newUser;
        Roles userRole;

        if (role != null && role.equalsIgnoreCase("COMPANY")) {
            newUser = new Company_entity();
            userRole = Roles.Company;
        } else if (role != null && role.equalsIgnoreCase("ADMIN")) {
            newUser = new User_entity();  // Админ использует базовую модель
            userRole = Roles.Admin;
        } else {
            newUser = new Student_entity();
            userRole = Roles.Student;
        }

        newUser.setEmail(email);
        newUser.setPasswordHash(passwordEncoder.encode(password));
        newUser.setRole(userRole);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(true);
        newUser.setTermsAccepted(false);
        newUser.setIpAddress(ipAddress);

        boolean registered = userService.registerUser(newUser);

        if (registered) {
            String token = jwtUtil.generateToken(email);
            System.out.println("✅ Успешная регистрация: " + email + " (Роль: " + userRole + ", IP: " + ipAddress + ")");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("role", userRole.name());
            response.put("message", "Регистрация успешна. Пожалуйста, примите пользовательское соглашение.");
            response.put("needAcceptTerms", true);
            response.put("userId", userService.getUserByEmail(email).getId().toString());

            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ошибка при регистрации пользователя"
            ));
        }
    }

    @PostMapping("/accept-terms")
    public ResponseEntity<?> acceptTerms(@RequestParam String userId) {
        UUID uuid = UUID.fromString(userId);
        boolean accepted = userService.acceptTerms(uuid);
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
    public ResponseEntity<?> checkTerms(@PathVariable String userId) {
        UUID uuid = UUID.fromString(userId);
        boolean accepted = userService.isTermsAccepted(uuid);
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
        User_entity user = userService.getUserByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("exists", exists);

        if (user != null) {
            response.put("role", user.getRole() != null ? user.getRole().name() : "STUDENT");
            response.put("termsAccepted", user.isTermsAccepted());
            response.put("isActive", user.isActive());
            response.put("userId", user.getId().toString());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}