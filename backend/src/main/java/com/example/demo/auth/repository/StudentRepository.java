package com.example.demo.auth.repository;

import com.example.demo.Models.Student_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student_entity, UUID> {
    Optional<Student_entity> findByEmail(String email);
}