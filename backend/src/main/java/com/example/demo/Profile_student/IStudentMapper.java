package com.example.demo.Profile_student;

import com.example.demo.Profile_student.DTOs.StudentRequestDto;
import com.example.demo.Profile_student.DTOs.StudentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface IStudentMapper {

    @Mapping(target = "Visibility", source = "Visibility")
    @Mapping(target = "Phone", source = "Phone")
    @Mapping(target = "Group", source = "Group")
    @Mapping(target = "Full_name", source = "Full_name")
    @Mapping(target = "Direction", source = "Direction")
    @Mapping(target = "Course", source = "Course")
    @Mapping(target = "Avatar_url", source = "Avatar_url")
    @Mapping(target = "User_ID", source = "User_ID")
    StudentResponseDto toDto(Student_entity entity);


    @Mapping(target = "visibility", source = "Visibility")
    @Mapping(target = "user_ID", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "phone", source = "Phone")
    @Mapping(target = "group", source = "Group")
    @Mapping(target = "full_name", source = "Full_name")
    @Mapping(target = "direction", source = "Direction")
    @Mapping(target = "course", source = "Course")
    @Mapping(target = "avatar_url", source = "Avatar_url")
    Student_entity toEntity(StudentRequestDto request);


    @Mapping(target = "visibility", source = "Visibility")
    @Mapping(target = "user_ID", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "phone", source = "Phone")
    @Mapping(target = "group", source = "Group")
    @Mapping(target = "full_name", source = "Full_name")
    @Mapping(target = "direction", source = "Direction")
    @Mapping(target = "course", source = "Course")
    @Mapping(target = "avatar_url", source = "Avatar_url")
    void updateEntity(StudentRequestDto request, @MappingTarget Student_entity entity);

    List<StudentResponseDto> toDtoList(List<Student_entity> entities);
}
