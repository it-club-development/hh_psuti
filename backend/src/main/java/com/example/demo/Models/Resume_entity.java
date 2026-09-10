package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name="Resumes")
@Getter
@Setter
@NoArgsConstructor
public class Resume_entity {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID ID;
        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId
        @JoinColumn(name = "User_ID")
        private UUID Student_ID;
        @Column(nullable = false)
        private String Skills;
        @Column(nullable = false)
        private String Portfolio_links;
        @Column(nullable = false)
        private String Grades_comment;
        @Column(nullable = false)
        private LocalDateTime Created_at;
        private LocalDateTime Updated_at;
}
