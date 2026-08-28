package com.example.demo.profile_company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data  // ← ДОБАВЛЕНО! Генерирует геттеры и сеттеры
public class CompanyProfileRequest {

    @NotBlank(message = "Название компании обязательно")
    private String companyName;

    private String description;
    private String website;
    private String logoUrl;

    @Email(message = "Неверный формат email")
    private String contactEmail;

    private String contactPhone;
    private String companyAddress;
    private String inn;
    private String industry;
    private Integer employeeCount;
}