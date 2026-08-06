package com.example.demo.auth.repository;

import com.example.demo.auth.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
        Optional<CompanyEntity> findByEmail(String email);
}
