package com.example.demo.Services;

import com.example.demo.Models.DTOs.Vacancy.VacancyRequestDto;
import com.example.demo.Models.DTOs.Vacancy.VacancyResponseDto;
import com.example.demo.Models.Mappers.IVacancyMapper;
import com.example.demo.Models.Vacancy_entity;
import com.example.demo.Repositories.IVacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VacancyService {
    private final IVacancyRepository _repository;
    private final IVacancyMapper _mapper;

    public VacancyService(IVacancyRepository repository, IVacancyMapper mapper) {
        _repository = repository;
        _mapper = mapper;
    }

    public List<VacancyResponseDto> findAll()
    {
        return _mapper.toDtoList(_repository.findAll());
    }

    public Optional<VacancyResponseDto> findById(UUID Id)
    {
        return _repository.findById(Id).map(_mapper::toDto);
    }

    public Optional<VacancyResponseDto> findByName(String Title)     {
        return _repository.findByTitle(Title).map(_mapper::toDto);
    }

    @Transactional
    public VacancyResponseDto create(VacancyRequestDto request) {
        Vacancy_entity response = _mapper.toEntity(request);
        Vacancy_entity created = _repository.create(response);
        return _mapper.toDto(created);
    }

    @Transactional
    public VacancyResponseDto update(UUID Id, VacancyRequestDto request) {
        Vacancy_entity vacancy = _repository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Вакансия не найдена: ID - " + Id));
        _mapper.updateEntity(request, vacancy);
        Vacancy_entity updated = _repository.create(vacancy);
        return _mapper.toDto(updated);
    }

    @Transactional
    public void delete(UUID Id) {
        _repository.deleteById(Id);
    }
}
