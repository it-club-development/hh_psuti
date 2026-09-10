package com.example.demo.Repositories;

import com.example.demo.Models.Vacancy_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IVacancyRepository extends JpaRepository<Vacancy_entity, UUID> {
    Optional<Vacancy_entity> findByTitle(String Title);
    Vacancy_entity create(Vacancy_entity entity);
}
