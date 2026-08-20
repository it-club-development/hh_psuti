package com.example.demo;

import com.example.demo.auth.controller.AuthController;
import com.example.demo.auth.security.JWT_util;
import com.example.demo.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserService userService;

    @Autowired
    private JWT_util jwtUtil;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(jwtUtil).isNotNull();
        System.out.println("✅ Контекст загружен успешно!");
    }

    @Test
    void jwtUtilShouldWork() {
        String token = jwtUtil.generateAccessToken("test@mail.ru", "STUDENT");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String email = jwtUtil.extractEmail(token);
        assertThat(email).isEqualTo("test@mail.ru");

        boolean valid = jwtUtil.validateToken(token);
        assertThat(valid).isTrue();

        System.out.println("✅ JWT работает!");
    }
}