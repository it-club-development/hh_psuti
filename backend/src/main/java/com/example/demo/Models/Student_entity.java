package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Student_entity extends User_entity {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "group_name")
    private String groupName;

    private Integer course;

    private String direction;

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "visibility")
    private String visibility = "PUBLIC";
}