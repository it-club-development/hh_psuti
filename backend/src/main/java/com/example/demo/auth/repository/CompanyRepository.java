package com.example.demo.auth.repository;

import com.example.demo.auth.model.СompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<СompanyEntity, Long> {
        Optional<СompanyEntity> findByEmail(String email);
}
