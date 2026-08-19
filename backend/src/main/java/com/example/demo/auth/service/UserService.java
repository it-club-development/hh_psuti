package com.example.demo.auth.service;

import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.user;
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

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public user getUser(String email) {
        return getUserByEmail(email);
    }

    public user getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public user getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String getPassword(String email) {
        user user = getUserByEmail(email);
        return user != null ? user.getPassword() : null;
    }

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

    @Transactional
    public void updateLastLogin(String email, String ipAddress) {
        user user = getUserByEmail(email);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setIpAddress(ipAddress);
            userRepository.save(user);
        }
    }

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

    public boolean isTermsAccepted(Long userId) {
        user user = getUserById(userId);
        return user != null && user.isTermsAccepted();
    }

    @Transactional
    public void changeRole(Long userId, Role role) {
        user user = getUserById(userId);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
    }
}