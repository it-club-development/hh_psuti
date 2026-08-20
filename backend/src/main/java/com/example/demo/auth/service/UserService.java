package com.example.demo.auth.service;

import com.example.demo.General.Roles;
import com.example.demo.Models.User_entity;
import com.example.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User_entity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User_entity getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public String getPassword(String email) {
        User_entity user = getUserByEmail(email);
        return user != null ? user.getPasswordHash() : null;
    }

    @Transactional
    public boolean registerUser(User_entity newUser) {
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

    @Transactional
    public void updateLastLogin(String email, String ipAddress) {
        User_entity user = getUserByEmail(email);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIpAddress(ipAddress);
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean acceptTerms(UUID userId) {
        User_entity user = getUserById(userId);
        if (user != null) {
            user.setTermsAccepted(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean isTermsAccepted(UUID userId) {
        User_entity user = getUserById(userId);
        return user != null && user.isTermsAccepted();
    }

    @Transactional
    public void changeRole(UUID userId, Roles role) {
        User_entity user = getUserById(userId);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
    }
}