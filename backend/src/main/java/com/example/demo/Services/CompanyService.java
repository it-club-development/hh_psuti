package com.example.demo.Services;

import com.example.demo.Models.Company_entity;
import com.example.demo.Models.DTOs.Company.CompanyRequestDto;
import com.example.demo.Models.DTOs.Company.CompanyResponseDto;
import com.example.demo.Models.Mappers.ICompanyMapper;
import com.example.demo.Repositories.ICompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CompanyService {
    private final ICompanyRepository _repository;
    private final ICompanyMapper _mapper;

    CompanyService(ICompanyRepository repository, ICompanyMapper mapper)
    {
        _repository = repository;
        _mapper = mapper;
    }

    public List<CompanyResponseDto> findAll()
    {
        return _mapper.toDtoList(_repository.findAll());
    }

    public Optional<CompanyResponseDto> findById(UUID Id)
    {
        return _repository.findById(Id).map(_mapper::toDto);
    }

    public Optional<CompanyResponseDto> findByName(String Name)
    {
        return _repository.findByName(Name).map(_mapper::toDto);
    }

    @Transactional
    public CompanyResponseDto create(CompanyRequestDto request) {
        Company_entity company = _mapper.toEntity(request);
        Company_entity created = _repository.create(company);
        return _mapper.toDto(created);
    }

    @Transactional
    public CompanyResponseDto update(UUID Id, CompanyRequestDto request) {
        Company_entity company = _repository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Компания не найдена: ID - " + Id));
        _mapper.updateEntity(request, company);
        Company_entity updated = _repository.create(company);
        return _mapper.toDto(updated);
    }

    @Transactional
    public void delete(UUID Id) {
        _repository.deleteById(Id);
    }
}
