package com.example.demo.Models.DTOs.Company;

import com.example.demo.Models.Company_entity;

/**
 * DTO for {@link Company_entity}
 */
public record CompanyRequestDto(String Name, String Site, String Description, String Logo) {
}

