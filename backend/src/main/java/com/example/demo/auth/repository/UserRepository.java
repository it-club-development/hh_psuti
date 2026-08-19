package com.example.demo.auth.repository;

import com.example.demo.auth.model.user;  // ← Импорт User, не StudentEntity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<user, Long> {  // ← User, не StudentEntity
    Optional<user> findByEmail(String email);

    boolean existsByEmail(String email);
}