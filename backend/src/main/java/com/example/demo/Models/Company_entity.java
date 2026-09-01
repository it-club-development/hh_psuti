package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="Companies")
@Getter
@Setter
@NoArgsConstructor
public class Company_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID User_ID;
    @Column(nullable = false, unique = true)
    private String Name;
    @Column(nullable = false, unique = true)
    private String Site;
    @Column(nullable = false)
    private String Description;
    @Column(unique = true)
    private String Logo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private User_entity User;
}
