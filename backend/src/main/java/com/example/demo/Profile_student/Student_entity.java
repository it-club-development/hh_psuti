package com.example.demo.Profile_student;

import com.example.demo.Models.User_entity;
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
public class Student_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID User_ID;
    @Column(nullable = false)
    private String Full_name;
    @Column(nullable = false)
    private String Group;
    @Column(nullable = false)
    private String Course;
    @Column(nullable = false)
    private String Direction;
    @Column(nullable = false,length = 12)
    private String Phone;
    @Column(unique = true)
    private String Avatar_url;
    private boolean Visibility;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private User_entity user;
}
