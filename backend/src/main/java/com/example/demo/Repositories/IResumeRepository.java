package com.example.demo.Repositories;

import com.example.demo.Models.Resume_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IResumeRepository extends JpaRepository<Resume_entity, UUID> {
    Resume_entity create(Resume_entity entity);
}
