package com.example.demo.aut;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    // Имитация базы данных
    private Map<String, String> users = new HashMap<>();

    public UserService() {
        // Тестовый пользователь (пароль: password123)
        // Хеш создан с помощью BCrypt
        /*
        как был создан хэш:
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("password123");
        // hash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBfUJ4u5k5yQdO"
        */
        users.put("test@mail.ru", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBfUJ4u5k5yQdO");
    }
    public boolean userExists(String email) {
        return users.containsKey(email);
    }
    public String getPassword(String email) {
        return users.get(email);
    }
}