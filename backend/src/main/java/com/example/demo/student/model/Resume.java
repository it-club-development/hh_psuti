package com.example.demo.student.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(columnDefinition = "TEXT")
    private String skills;  // Навыки

    @Column(name = "portfolio_links", columnDefinition = "TEXT")
    private String portfolioLinks;  // Ссылки на портфолио

    @Column(name = "grades_comment", columnDefinition = "TEXT")
    private String gradesComment;  // Успеваемость

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}