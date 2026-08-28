package com.example.demo.profile_company.service;

import com.example.demo.Models.User_entity;
import com.example.demo.auth.service.UserService;
import com.example.demo.profile_company.dto.CompanyProfileRequest;
import com.example.demo.profile_company.dto.CompanyProfileResponse;
import com.example.demo.profile_company.model.CompanyProfile;
import com.example.demo.profile_company.repository.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CompanyProfileService {

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    @Autowired
    private UserService userService;

    // @Cacheable — ОТКЛЮЧАЕМ
    public CompanyProfileResponse getProfile(UUID userId) {
        User_entity user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        CompanyProfile profile = companyProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Профиль компании не найден"));

        return mapToResponse(profile, user);
    }

    @Transactional
    // @CacheEvict — ОТКЛЮЧАЕМ
    public CompanyProfileResponse createOrUpdateProfile(UUID userId, CompanyProfileRequest request) {
        User_entity user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        CompanyProfile profile = companyProfileRepository.findByUser(user)
                .orElse(new CompanyProfile());

        profile.setUser(user);
        profile.setCompanyName(request.getCompanyName());
        profile.setDescription(request.getDescription());
        profile.setWebsite(request.getWebsite());
        profile.setLogoUrl(request.getLogoUrl());
        profile.setContactPhone(request.getContactPhone());
        profile.setContactEmail(request.getContactEmail());
        profile.setCompanyAddress(request.getCompanyAddress());
        profile.setInn(request.getInn());
        profile.setIndustry(request.getIndustry());
        profile.setEmployeeCount(request.getEmployeeCount());

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(LocalDateTime.now());
        }
        profile.setUpdatedAt(LocalDateTime.now());

        CompanyProfile saved = companyProfileRepository.save(profile);
        return mapToResponse(saved, user);
    }

    public boolean profileExists(UUID userId) {
        return companyProfileRepository.existsByUser_Id(userId);
    }

    private CompanyProfileResponse mapToResponse(CompanyProfile profile, User_entity user) {
        CompanyProfileResponse response = new CompanyProfileResponse();
        response.setId(profile.getId());
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setCompanyName(profile.getCompanyName());
        response.setDescription(profile.getDescription());
        response.setWebsite(profile.getWebsite());
        response.setLogoUrl(profile.getLogoUrl());
        response.setContactPhone(profile.getContactPhone());
        response.setContactEmail(profile.getContactEmail());
        response.setCompanyAddress(profile.getCompanyAddress());
        response.setInn(profile.getInn());
        response.setIndustry(profile.getIndustry());
        response.setEmployeeCount(profile.getEmployeeCount());
        response.setVerified(profile.isVerified());
        response.setActive(user.isActive());
        response.setTermsAccepted(user.isTermsAccepted());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}