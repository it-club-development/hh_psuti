package com.example.demo.Models;

import com.example.demo.General.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
public class User_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;
    @Column(nullable = false, unique = true)
    private String Email;
    @Column(nullable = false)
    private Roles Role; //General
    @Column(nullable = false)
    private LocalDateTime Created_at;
    @Column(nullable = false)
    private LocalDateTime Last_login;
    private String Password_hash;
}
