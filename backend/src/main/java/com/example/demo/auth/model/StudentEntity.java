package com.example.demo.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class StudentEntity extends user {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "group_name")
    private String groupName;

    private Integer course;

    private String direction;  // Направление подготовки

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String visibility = "PUBLIC";  // PUBLIC / COMPANY_ONLY
}