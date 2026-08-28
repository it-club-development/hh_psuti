package com.example.demo.profile_company.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileResponse {

    private UUID id;
    private UUID userId;
    private String email;
    private String companyName;
    private String description;
    private String website;
    private String logoUrl;
    private String contactPhone;
    private String contactEmail;
    private String companyAddress;
    private String inn;
    private String industry;
    private Integer employeeCount;
    private boolean isVerified;
    private boolean isActive;
    private boolean termsAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}