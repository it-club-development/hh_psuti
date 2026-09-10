package com.example.demo.Models;

import com.example.demo.General.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Responses")
@Getter
@Setter
@NoArgsConstructor
public class Response_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private UUID Student_ID;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private UUID Vacancy_ID;
    @Column(nullable = false)
    private Status Status;
    @Column(nullable = false)
    private String Cover_letter;
    @Column(nullable = false)
    private LocalDateTime Created_at;
    private LocalDateTime Updated_at;

}
