package com.example.demo;

import com.example.demo.auth.controller.AuthController;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.user;
import com.example.demo.auth.security.JWT_util;
import com.example.demo.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    // ===== ТЕСТ 2: JWT утилита работает =====
    @Test
    void jwtUtilShouldWork() throws Exception {
        String token = jwtUtil.generateAccessToken("test@mail.ru", "STUDENT");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String email = jwtUtil.extractEmail(token);
        assertThat(email).isEqualTo("test@mail.ru");

        boolean valid = jwtUtil.validateToken(token);
        assertThat(valid).isTrue();

        System.out.println("✅ JWT работает!");
    }

    // ===== ТЕСТ 3: Проверка существования пользователя =====
    @Test
    void userExistsShouldWork() {
        boolean exists = userService.userExists("test@mail.ru");
        assertThat(exists).isTrue();
        System.out.println("✅ Пользователь test@mail.ru существует!");
    }

    // ===== ТЕСТ 4: Проверка несуществующего пользователя =====
    @Test
    void userNotExistsShouldWork() {
        boolean exists = userService.userExists("unknown@mail.ru");
        assertThat(exists).isFalse();
        System.out.println("✅ Пользователь unknown@mail.ru не существует!");
    }

    // ===== ТЕСТ 5: Регистрация нового пользователя =====
    @Test
    void registerUserShouldWork() {
        String testEmail = "newuser" + System.currentTimeMillis() + "@mail.ru";

        user newUser = new user();
        newUser.setEmail(testEmail);
        newUser.setPassword("123456");
        newUser.setRole(Role.STUDENT);

        boolean registered = userService.registerUser(newUser);
        assertThat(registered).isTrue();
        assertThat(userService.userExists(testEmail)).isTrue();

        System.out.println("✅ Новый пользователь зарегистрирован: " + testEmail);
    }

    // ===== ТЕСТ 6: REST API - Регистрация =====
    @Test
    void registerEndpointShouldWork() throws Exception {
        String testEmail = "apiuser" + System.currentTimeMillis() + "@mail.ru";

        Map<String, String> request = new HashMap<>();
        request.put("email", testEmail);
        request.put("password", "123456");
        request.put("confirmPassword", "123456");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.message").value("Регистрация успешна. Пожалуйста, примите пользовательское соглашение."))
                .andExpect(jsonPath("$.needAcceptTerms").value(true));

        System.out.println("✅ REST API регистрация работает!");
    }

    // ===== ТЕСТ 7: REST API - Вход =====
    @Test
    void loginEndpointShouldWork() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@mail.ru");
        request.put("password", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.message").value("Успешный вход"))
                .andExpect(jsonPath("$.token").exists());

        System.out.println("✅ REST API вход работает!");
    }

    // ===== ТЕСТ 8: REST API - Принятие соглашения =====
    @Test
    void acceptTermsEndpointShouldWork() throws Exception {
        String testEmail = "termsapiuser" + System.currentTimeMillis() + "@mail.ru";

        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("email", testEmail);
        registerRequest.put("password", "123456");
        registerRequest.put("confirmPassword", "123456");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        Integer userId = (Integer) responseMap.get("userId");

        mockMvc.perform(post("/api/auth/accept-terms")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termsAccepted").value(true));

        System.out.println("✅ REST API принятие соглашения работает!");
    }

    // ===== ТЕСТ 9: REST API - Проверка IP =====
    @Test
    void getIpEndpointShouldWork() throws Exception {
        mockMvc.perform(get("/api/auth/my-ip"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        System.out.println("✅ REST API проверка IP работает!");
    }

    // ===== ТЕСТ 10: REST API - Проверка пользователя =====
    @Test
    void checkUserEndpointShouldWork() throws Exception {
        mockMvc.perform(get("/api/auth/check/test@mail.ru"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.exists").value(true));

        System.out.println("✅ REST API проверка пользователя работает!");
    }

    // ===== ТЕСТ 11: REST API - Проверка соглашения =====
    @Test
    void checkTermsEndpointShouldWork() throws Exception {
        String testEmail = "checkterms" + System.currentTimeMillis() + "@mail.ru";

        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("email", testEmail);
        registerRequest.put("password", "123456");
        registerRequest.put("confirmPassword", "123456");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        Integer userId = (Integer) responseMap.get("userId");

        // Проверяем, что соглашение не принято
        mockMvc.perform(get("/api/auth/check-terms/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termsAccepted").value(false));

        // Принимаем соглашение
        mockMvc.perform(post("/api/auth/accept-terms")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());

        // Проверяем, что соглашение принято
        mockMvc.perform(get("/api/auth/check-terms/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termsAccepted").value(true));

        System.out.println("✅ REST API проверка соглашения работает!");
    }
}