package com.example.demo.auth.repository;

import com.example.demo.Models.Company_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company_entity, UUID> {
        Optional<Company_entity> findByEmail(String email);
}