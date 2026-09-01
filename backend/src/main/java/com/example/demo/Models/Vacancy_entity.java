package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Vacancies")
@Getter
@Setter
@NoArgsConstructor
public class Vacancy_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private UUID Company_ID;
    @Column(nullable = false)
    private String Title;
    @Column(nullable = false)
    private String Description;
    private Double Salary; //Или Decimal?
    @Column(nullable = false, length = 30)
    private String City;
    @Column(nullable = false)
    private String Employment_type;
    @Column(nullable = false)
    private boolean Is_active;
    @Column(updatable = false, nullable = false)
    private LocalDateTime Created_at;
}
