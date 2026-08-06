package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.*;


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
    private String Full_name;
    private String Group;
    private String Course;
    private String Direction;
    private String Phone;
    private String Avatar_url;
    private boolean Visibility;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private User_entity user;
}
