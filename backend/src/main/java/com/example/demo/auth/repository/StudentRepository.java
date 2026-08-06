package com.example.demo.auth.repository;

import com.example.demo.auth.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface  StudentRepository extends JpaRepository<StudentEntity, Long>{
    Optional<StudentEntity> findByEmail(String email);
}
