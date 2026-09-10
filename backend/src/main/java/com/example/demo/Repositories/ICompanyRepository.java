package com.example.demo.Repositories;

import com.example.demo.Models.Company_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICompanyRepository extends JpaRepository<Company_entity, UUID> {

    Optional<Company_entity> findByName(String Name);
    Company_entity create(Company_entity entity);
}
