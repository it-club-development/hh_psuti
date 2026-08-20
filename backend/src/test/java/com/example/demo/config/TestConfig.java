package com.example.demo.config;

import com.example.demo.General.Roles;
import com.example.demo.Models.User_entity;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public UserService userService() {
        return new UserService() {
            private Map<String, User_entity> users = new HashMap<>();

            {
                // Тестовый пользователь
                User_entity testUser = new User_entity();
                testUser.setId(UUID.randomUUID());
                testUser.setEmail("test@mail.ru");
                testUser.setPasswordHash("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBfUJ4u5k5yQdO");
                testUser.setRole(Roles.Student);
                testUser.setCreatedAt(LocalDateTime.now());
                testUser.setTermsAccepted(true);
                users.put("test@mail.ru", testUser);
            }

            @Override
            public boolean userExists(String email) {
                return users.containsKey(email);
            }

            @Override
            public User_entity getUserByEmail(String email) {
                return users.get(email);
            }

            @Override
            public User_entity getUserById(UUID id) {
                return users.values().stream()
                        .filter(u -> u.getId() != null && u.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public String getPassword(String email) {
                User_entity user = users.get(email);
                return user != null ? user.getPasswordHash() : null;
            }

            @Override
            public boolean registerUser(User_entity newUser) {
                String email = newUser.getEmail();
                if (users.containsKey(email)) {
                    return false;
                }
                newUser.setId(UUID.randomUUID());
                newUser.setCreatedAt(LocalDateTime.now());
                users.put(email, newUser);
                return true;
            }

            @Override
            public void updateLastLogin(String email, String ipAddress) {
                User_entity user = users.get(email);
                if (user != null) {
                    user.setLastLogin(LocalDateTime.now());
                    user.setIpAddress(ipAddress);
                }
            }

            @Override
            public boolean acceptTerms(UUID userId) {
                User_entity user = getUserById(userId);
                if (user != null) {
                    user.setTermsAccepted(true);
                    return true;
                }
                return false;
            }

            @Override
            public boolean isTermsAccepted(UUID userId) {
                User_entity user = getUserById(userId);
                return user != null && user.isTermsAccepted();
            }

            @Override
            public void changeRole(UUID userId, Roles role) {
                User_entity user = getUserById(userId);
                if (user != null) {
                    user.setRole(role);
                }
            }
        };
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}