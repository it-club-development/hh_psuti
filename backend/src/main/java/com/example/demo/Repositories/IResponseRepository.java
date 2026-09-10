package com.example.demo.Repositories;

import com.example.demo.Models.Response_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IResponseRepository extends JpaRepository<Response_entity, UUID> {
    Response_entity create(Response_entity entity);
}
