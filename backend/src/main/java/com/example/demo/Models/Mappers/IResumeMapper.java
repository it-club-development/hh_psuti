package com.example.demo.Models.Mappers;

import com.example.demo.Models.DTOs.Resume.ResumeRequestDto;
import com.example.demo.Models.DTOs.Resume.ResumeResponseDto;
import com.example.demo.Models.Resume_entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IResumeMapper {

    @Mapping(target = "Skills", source = "Skills")
    @Mapping(target = "Portfolio_links", source = "Portfolio_links")
    @Mapping(target = "Grades_comment", source = "Grades_comment")
    @Mapping(target = "Updated_at", source = "Updated_at")
    @Mapping(target = "Student_ID", source = "Student_ID")
    @Mapping(target = "Created_at", source = "Created_at")
    ResumeResponseDto toDto(Resume_entity entity);

    @Mapping(target = "skills", source = "Skills")
    @Mapping(target = "portfolio_links", source = "Portfolio_links")
    @Mapping(target = "grades_comment", source = "Grades_comment")
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "student_ID", source = "Student_ID")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "ID", ignore = true)
    Resume_entity toEntity(ResumeRequestDto request);

    @Mapping(target = "skills", source = "Skills")
    @Mapping(target = "portfolio_links", source = "Portfolio_links")
    @Mapping(target = "grades_comment", source = "Grades_comment")
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "student_ID", source = "Student_ID")
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "ID", ignore = true)
    void updateEntity(ResumeRequestDto request, @MappingTarget Resume_entity entity);

    List<ResumeResponseDto> toDtoList(List<Resume_entity> entities);
}
