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

    // ===== ТЕСТ 1: Контекст загружается =====
    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(jwtUtil).isNotNull();
        System.out.println("✅ Контекст загружен успешно!");
    }

    // ===== ТЕСТ 2: Контроллер существует =====
    @Test
    void authControllerShouldBeLoaded() {
        assertThat(authController).isNotNull();
        System.out.println("✅ Контроллер загружен!");
    }

    // ===== ТЕСТ 3: JWT утилита работает =====
    @Test
    void jwtUtilShouldWork() {
        String token = jwtUtil.generateToken("test@mail.ru");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String email = jwtUtil.extractEmail(token);
        assertThat(email).isEqualTo("test@mail.ru");

        boolean valid = jwtUtil.validateToken(token);
        assertThat(valid).isTrue();

        System.out.println("✅ JWT работает: token = " + token.substring(0, 30) + "...");
    }

    // ===== ТЕСТ 4: Проверка существования пользователя =====
    @Test
    void userExistsShouldWork() {
        boolean exists = userService.userExists("test@mail.ru");
        assertThat(exists).isTrue();
        System.out.println("✅ Пользователь test@mail.ru существует!");
    }

    // ===== ТЕСТ 5: Проверка несуществующего пользователя =====
    @Test
    void userNotExistsShouldWork() {
        boolean exists = userService.userExists("unknown@mail.ru");
        assertThat(exists).isFalse();
        System.out.println("✅ Пользователь unknown@mail.ru не существует!");
    }

    // ===== ТЕСТ 6: Получение пароля =====
    @Test
    void getPasswordShouldWork() {
        String password = userService.getPassword("test@mail.ru");
        assertThat(password).isNotNull();
        assertThat(password).isNotEmpty();
        System.out.println("✅ Пароль получен!");
    }

    // ===== ТЕСТ 7: Получение несуществующего пароля =====
    @Test
    void getPasswordShouldReturnNullForUnknown() {
        String password = userService.getPassword("unknown@mail.ru");
        assertThat(password).isNull();
        System.out.println("✅ Для неизвестного пользователя пароль = null");
    }

    // ===== ТЕСТ 8: Регистрация нового пользователя =====
    @Test
    void registerUserShouldWork() {
        com.example.demo.auth.model.user newUser =
                new com.example.demo.auth.model.user("newuser@mail.ru", "123456");

        boolean registered = userService.registerUser(newUser);
        assertThat(registered).isTrue();
        assertThat(userService.userExists("newuser@mail.ru")).isTrue();

        System.out.println("✅ Новый пользователь зарегистрирован!");
    }

    // ===== ТЕСТ 9: Обновление последнего входа =====
    @Test
    void updateLastLoginShouldWork() {
        userService.updateLastLogin("test@mail.ru", "192.168.1.1");

        com.example.demo.auth.model.user user = userService.getUser("test@mail.ru");
        assertThat(user.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(user.getLastLogin()).isNotNull();

        System.out.println("✅ Последний вход обновлён!");
    }
}