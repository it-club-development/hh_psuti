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
    private UUID Company_ID;
    private String Title;
    private String Description;
    private Double Salary; //Или Decimal?
    private String City;
    private String Employement_type;
    private boolean Is_active;
    private LocalDateTime Created_at;
}
