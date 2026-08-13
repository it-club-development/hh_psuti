package com.example.demo.auth.controller;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.model.CompanyEntity;
import com.example.demo.auth.model.StudentEntity;
import com.example.demo.auth.model.user;
import com.example.demo.auth.repository.CompanyRepository;
import com.example.demo.auth.repository.StudentRepository;
import com.example.demo.auth.security.IpUtil;
import com.example.demo.auth.security.JWT_util;
import com.example.demo.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    private IpUtil ipUtil;  // ← ДЛЯ ПОЛУЧЕНИЯ IP

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody user loginRequest,
                                   HttpServletRequest request) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Получаем IP адрес
        String ipAddress = ipUtil.getClientIp((javax.servlet.http.HttpServletRequest) request);
        System.out.println("🔐 Попытка входа с IP: " + ipAddress);

        // Проверка существования пользователя
        if (!userService.userExists(email)) {
            System.out.println("❌ Пользователь не найден: " + email);
            return ResponseEntity.status(401).body("Пользователь не найден");
        }

        // Проверка пароля
        String storedPassword = userService.getPassword(email);
        if (!passwordEncoder.matches(password, storedPassword)) {
            System.out.println("❌ Неверный пароль для: " + email);
            return ResponseEntity.status(401).body("Неверный пароль");
        }

        // Обновляем IP и время последнего входа
        userService.updateLastLogin(email, ipAddress);

        // Генерация токена
        String token = jwtUtil.generateToken(email);
        System.out.println("✅ Успешный вход: " + email + " (IP: " + ipAddress + ")");

        return ResponseEntity.ok(token);
    }

    // Дополнительный эндпоинт для проверки IP
    @GetMapping("/my-ip")
    public ResponseEntity<?> getMyIp(HttpServletRequest request) {
        String ip = ipUtil.getClientIp((javax.servlet.http.HttpServletRequest) request);
        return ResponseEntity.ok("Ваш IP: " + ip);
    }
}