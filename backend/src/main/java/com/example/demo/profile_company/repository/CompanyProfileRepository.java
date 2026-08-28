package com.example.demo.profile_company.repository;

import com.example.demo.Models.User_entity;
import com.example.demo.profile_company.model.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, UUID> {

    Optional<CompanyProfile> findByUser(User_entity user);

    Optional<CompanyProfile> findByUser_Id(UUID userId);

    boolean existsByUser_Id(UUID userId);
}