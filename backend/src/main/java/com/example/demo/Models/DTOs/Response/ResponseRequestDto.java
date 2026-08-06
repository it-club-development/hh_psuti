package com.example.demo.Models.DTOs.Response;

import com.example.demo.Models.Response_entity;

import java.util.UUID;

/**
 * DTO for {@link Response_entity}
 */
public record ResponseRequestDto(UUID Student_ID, UUID Vacancy_ID, boolean Status, String Cover_letter) {
}