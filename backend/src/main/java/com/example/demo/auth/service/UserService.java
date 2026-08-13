package com.example.demo.auth.service;
import com.example.demo.auth.model.user;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {
    // Имитация базы данных
    private Map<String, user> users = new HashMap<>();

    public UserService() {
        // Тестовый пользователь
        user testUser = new user("test@mail.ru", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBfUJ4u5k5yQdO");
        testUser.setIpAddress("127.0.0.1");
        testUser.setCreatedAt(LocalDateTime.now());
        users.put("test@mail.ru", testUser);
    }

    public boolean userExists(String email) {
        return users.containsKey(email);
    }

    public String getPassword(String email) {
        user user = users.get(email);
        return user != null ? user.getPassword() : null;
    }

    public user getUser(String email) {
        return users.get(email);
    }

    public void updateLastLogin(String email, String ipAddress) {
        user user = users.get(email);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIpAddress(ipAddress);
        }
    }
}