package com.example.demo.response.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Data
@NoArgsConstructor
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "vacancy_id")
    private Long vacancyId;

    @Enumerated(EnumType.STRING)
    private ResponseStatus status = ResponseStatus.PENDING;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

enum ResponseStatus {
    PENDING,      // На рассмотрении
    INVITED,      // Приглашён
    REJECTED,     // Отказано
    ACCEPTED      // Принят
}