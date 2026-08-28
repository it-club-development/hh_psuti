package com.example.demo.profile_company.controller;

import com.example.demo.profile_company.dto.CompanyProfileRequest;
import com.example.demo.profile_company.dto.CompanyProfileResponse;
import com.example.demo.profile_company.service.CompanyProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor  // ← ДОБАВЛЕНО! Создаёт конструктор с companyProfileService
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;  // ← теперь инициализируется через конструктор

    /**
     * Получить профиль компании
     * Доступ: COMPANY, ADMIN
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<?> getProfile(@RequestParam UUID userId) {
        try {
            CompanyProfileResponse profile = companyProfileService.getProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Создать или обновить профиль компании
     * Доступ: COMPANY, ADMIN
     */
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<?> createOrUpdateProfile(
            @RequestParam UUID userId,
            @Valid @RequestBody CompanyProfileRequest request) {
        try {
            CompanyProfileResponse response = companyProfileService.createOrUpdateProfile(userId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Проверить существование профиля компании
     */
    @GetMapping("/profile/exists")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<?> profileExists(@RequestParam UUID userId) {
        boolean exists = companyProfileService.profileExists(userId);
        return ResponseEntity.ok(Map.of(
                "exists", exists
        ));
    }
}