package com.example.demo.Models.Mappers;

import com.example.demo.Models.DTOs.Response.ResponseRequestDto;
import com.example.demo.Models.DTOs.Response.ResponseResponseDto;
import com.example.demo.Models.Response_entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IResponseMapper {

    @Mapping(target = "Vacancy_ID", source = "Vacancy_ID")
    @Mapping(target = "Updated_at", source = "Updated_at")
    @Mapping(target = "Student_ID", source = "Student_ID")
    @Mapping(target = "Status", source = "Status")
    @Mapping(target = "Created_at", source = "Created_at")
    @Mapping(target = "Cover_letter", source = "Cover_letter")
    ResponseResponseDto toDto(Response_entity entity);

    @Mapping(target = "vacancy_ID", source = "Vacancy_ID")
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "student_ID", source = "Student_ID")
    @Mapping(target = "status", source = "Status")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "cover_letter", source = "Cover_letter")
    @Mapping(target = "ID", ignore = true)
    Response_entity toEntity(ResponseRequestDto request);

    @Mapping(target = "vacancy_ID", source = "Vacancy_ID")
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "student_ID", source = "Student_ID")
    @Mapping(target = "status", source = "Status")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "cover_letter", source = "Cover_letter")
    @Mapping(target = "ID", ignore = true)
    void updateEntity(ResponseRequestDto request, @MappingTarget Response_entity entity);

    List<ResponseResponseDto> toDtoList(List<Response_entity> entities);
}
