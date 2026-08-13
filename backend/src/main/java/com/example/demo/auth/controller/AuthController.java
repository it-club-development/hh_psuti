package com.example.demo.auth.controller;

import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.model.user;
import com.example.demo.auth.security.IpUtil;
import com.example.demo.auth.security.JWT_util;
import com.example.demo.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;  // ← ИСПРАВЛЕНО
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWT_util jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IpUtil ipUtil;

    // ===== ЛОГИН =====
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody user loginRequest,
                                   HttpServletRequest request) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Получаем IP адрес (БЕЗ ПРИВЕДЕНИЯ ТИПОВ)
        String ipAddress = ipUtil.getClientIp(request);
        System.out.println("🔐 Попытка входа с IP: " + ipAddress);

        if (!userService.userExists(email)) {
            System.out.println("❌ Пользователь не найден: " + email);
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Пользователь не найден"
            ));
        }

        String storedPassword = userService.getPassword(email);
        if (!passwordEncoder.matches(password, storedPassword)) {
            System.out.println("❌ Неверный пароль для: " + email);
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Неверный пароль"
            ));
        }

        userService.updateLastLogin(email, ipAddress);
        String token = jwtUtil.generateToken(email);
        System.out.println("✅ Успешный вход: " + email + " (IP: " + ipAddress + ")");

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("message", "Успешный вход");

        return ResponseEntity.ok(response);
    }

    // ===== РЕГИСТРАЦИЯ =====
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest,
                                      HttpServletRequest request) {

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

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
            System.out.println("❌ Пользователь уже существует: " + email);
            return ResponseEntity.status(409).body(Map.of(
                    "error", "Пользователь с таким email уже существует"
            ));
        }

        user newUser = new user(email, password);
        newUser.setIpAddress(ipAddress);

        boolean registered = userService.registerUser(newUser);

        if (registered) {
            String token = jwtUtil.generateToken(email);
            System.out.println("✅ Успешная регистрация: " + email + " (IP: " + ipAddress + ")");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("message", "Регистрация успешно завершена");

            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ошибка при регистрации пользователя"
            ));
        }
    }

    // ===== ПРОВЕРКА IP =====
    @GetMapping("/my-ip")
    public ResponseEntity<?> getMyIp(HttpServletRequest request) {
        String ip = ipUtil.getClientIp(request);  // БЕЗ ПРИВЕДЕНИЯ ТИПОВ
        return ResponseEntity.ok(Map.of(
                "ip", ip,
                "timestamp", LocalDateTime.now()
        ));
    }

    // ===== ПРОВЕРКА ПОЛЬЗОВАТЕЛЯ =====
    @GetMapping("/check/{email}")
    public ResponseEntity<?> checkUser(@PathVariable String email) {
        boolean exists = userService.userExists(email);
        return ResponseEntity.ok(Map.of(
                "email", email,
                "exists", exists
        ));
    }
}
