package com.example.demo.Models.DTOs.Company;

import java.util.UUID;

public record CompanyResponseDto(UUID ID, String Name, String Site, String Description, String Logo) {}
