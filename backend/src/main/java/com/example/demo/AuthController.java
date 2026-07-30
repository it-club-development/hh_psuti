package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWT_util jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody user loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Проверка существования пользователя
        if (!userService.userExists(email)) {
            return ResponseEntity.status(401).body("Пользователь не найден");
        }

        // Проверка пароля
        String storedPassword = userService.getPassword(email);
        if (!passwordEncoder.matches(password, storedPassword)) {
            return ResponseEntity.status(401).body("Неверный пароль");
        }

        // Генерация токена
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(token);
    }
}