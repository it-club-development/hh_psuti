package com.example.demo.auth.service;

import com.example.demo.auth.model.user;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===== ПРОВЕРКА СУЩЕСТВОВАНИЯ =====
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // ===== ПОЛУЧЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО EMAIL =====
    public user getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ===== ПОЛУЧЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО ID =====
    public user getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ===== ПОЛУЧЕНИЕ ПАРОЛЯ =====
    public String getPassword(String email) {
        user user = getUserByEmail(email);
        return user != null ? user.getPassword() : null;
    }

    // ===== РЕГИСТРАЦИЯ =====
    @Transactional
    public boolean registerUser(user newUser) {
        String email = newUser.getEmail();

        if (userExists(email)) {
            return false;
        }

        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(true);
        newUser.setTermsAccepted(false);

        userRepository.save(newUser);
        return true;
    }

    // ===== ОБНОВЛЕНИЕ ПОСЛЕДНЕГО ВХОДА =====
    @Transactional
    public void updateLastLogin(String email, String ipAddress) {
        user user = getUserByEmail(email);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIpAddress(ipAddress);
            userRepository.save(user);
        }
    }

    // ===== ПРИНЯТИЕ СОГЛАШЕНИЯ =====
    @Transactional
    public boolean acceptTerms(Long userId) {
        user user = getUserById(userId);
        if (user != null) {
            user.setTermsAccepted(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // ===== ПРОВЕРКА СОГЛАШЕНИЯ =====
    public boolean isTermsAccepted(Long userId) {
        user user = getUserById(userId);
        return user != null && user.isTermsAccepted();
    }

    // ===== ИЗМЕНЕНИЕ РОЛИ =====
    @Transactional
    public void changeRole(Long userId, Role role) {
        user user = getUserById(userId);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
    }
}