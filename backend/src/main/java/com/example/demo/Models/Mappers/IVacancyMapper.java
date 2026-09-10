package com.example.demo.Models.Mappers;

import com.example.demo.Models.DTOs.Vacancy.VacancyRequestDto;
import com.example.demo.Models.DTOs.Vacancy.VacancyResponseDto;
import com.example.demo.Models.Vacancy_entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IVacancyMapper {

    @Mapping(target = "Title", source = "Title")
    @Mapping(target = "Salary", source = "Salary")
    @Mapping(target = "Is_active", source = "Is_active")
    @Mapping(target = "Employment_type", source = "Employment_type")
    @Mapping(target = "Description", source = "Description")
    @Mapping(target = "Created_at", source = "Created_at")
    @Mapping(target = "Company_ID", source = "Company_ID")
    @Mapping(target = "City", source = "City")
    VacancyResponseDto toDto(Vacancy_entity entity);

    @Mapping(target = "title", source = "Title")
    @Mapping(target = "salary", source = "Salary")
    @Mapping(target = "is_active", source = "Is_active")
    @Mapping(target = "employment_type", source = "Employment_type")
    @Mapping(target = "description", source = "Description")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "company_ID", ignore = true)
    @Mapping(target = "city", source = "City")
    @Mapping(target = "ID", ignore = true)
    Vacancy_entity toEntity(VacancyRequestDto request);

    @Mapping(target = "title", source = "Title")
    @Mapping(target = "salary", source = "Salary")
    @Mapping(target = "is_active", source = "Is_active")
    @Mapping(target = "employment_type", source = "Employment_type")
    @Mapping(target = "description", source = "Description")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "company_ID", ignore = true)
    @Mapping(target = "city", source = "City")
    @Mapping(target = "ID", ignore = true)
    void updateEntity(VacancyRequestDto request, @MappingTarget Vacancy_entity entity);

    List<VacancyResponseDto> toDtoList(List<Vacancy_entity> entities);
}
