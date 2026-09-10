package com.example.demo.Models.DTOs.Vacancy;

public record VacancyRequestDto(String Title, String Description, Double Salary, String City, String Employment_type, boolean Is_active) {
}
