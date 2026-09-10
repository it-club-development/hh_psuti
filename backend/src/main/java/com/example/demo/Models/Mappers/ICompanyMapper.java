package com.example.demo.Models.Mappers;

import com.example.demo.Models.Company_entity;
import com.example.demo.Models.DTOs.Company.CompanyRequestDto;
import com.example.demo.Models.DTOs.Company.CompanyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICompanyMapper {

    @Mapping(target = "Site", source = "Site")
    @Mapping(target = "Name", source = "Name")
    @Mapping(target = "Logo", source = "Logo")
    @Mapping(target = "Description", source = "Description")
    @Mapping(source = "User_ID", target = "User_ID")   // если в DTO поле называется id
    CompanyResponseDto toDto(Company_entity entity);

    @Mapping(target = "site", source = "Site")
    @Mapping(target = "name", source = "Name")
    @Mapping(target = "logo", source = "Logo")
    @Mapping(target = "description", source = "Description")
    @Mapping(target = "user_ID", ignore = true)
    @Mapping(target = "user", ignore = true)
    Company_entity toEntity(CompanyRequestDto request);

    @Mapping(target = "site", source = "Site")
    @Mapping(target = "name", source = "Name")
    @Mapping(target = "logo", source = "Logo")
    @Mapping(target = "description", source = "Description")
    @Mapping(target = "user_ID", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(CompanyRequestDto request, @MappingTarget Company_entity entity);
    List<CompanyResponseDto> toDtoList(List<Company_entity> entities);
}
