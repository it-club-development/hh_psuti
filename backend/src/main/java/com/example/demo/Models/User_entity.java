package com.example.demo.Models;

import com.example.demo.General.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User_entity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "terms_accepted")
    private boolean termsAccepted = false;

    // ===== КОНСТРУКТОР С ПАРАМЕТРАМИ (дополнительный) =====
    public User_entity(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.termsAccepted = false;
    }

    // Геттеры и сеттеры генерируются через Lombok (@Getter, @Setter)
}