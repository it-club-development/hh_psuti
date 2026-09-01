package com.example.demo.Models.DTOs.Vacancy;

import java.time.LocalDateTime;
import java.util.UUID;

public record VacancyResponseDto(UUID ID, UUID Company_ID, String Title, String Description, Double Salary, String City, String Employment_type, boolean Is_active, LocalDateTime Created_at) {
}
