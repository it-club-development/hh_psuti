package com.example.demo.auth.repository;

import com.example.demo.Models.User_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User_entity, UUID> {
    Optional<User_entity> findByEmail(String email);

    boolean existsByEmail(String email);
}