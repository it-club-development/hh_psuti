package com.example.demo.Profile_student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IStudentRepository extends JpaRepository<Student_entity, UUID> {
    Optional<Student_entity> findByName(String Name);
    Student_entity create(Student_entity entity);
}
