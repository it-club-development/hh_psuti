package com.example.demo.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "sso_id", unique = true)
    private String ssoId;  // ID из SSO

    private String password;  // Для тестовой версии

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "terms_accepted")
    private boolean termsAccepted = false;  // Пользовательское соглашение

    @Column(name = "ip_address")
    private String ipAddress;
}