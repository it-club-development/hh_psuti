package com.example.demo.Services;

import com.example.demo.Models.DTOs.Resume.ResumeRequestDto;
import com.example.demo.Models.DTOs.Resume.ResumeResponseDto;
import com.example.demo.Models.Mappers.IResumeMapper;
import com.example.demo.Models.Resume_entity;
import com.example.demo.Repositories.IResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ResumeService {

    private final IResumeRepository _repository;
    private final IResumeMapper _mapper;

    ResumeService(IResumeMapper mapper, IResumeRepository repository)
    {
        _mapper = mapper;
        _repository = repository;
    }

    public List<ResumeResponseDto> findAll()
    {
        return _mapper.toDtoList(_repository.findAll());
    }

    public Optional<ResumeResponseDto> findById(UUID Id)
    {
        return _repository.findById(Id).map(_mapper::toDto);
    }


    @Transactional
    public ResumeResponseDto create(ResumeRequestDto request) {
        Resume_entity response = _mapper.toEntity(request);
        Resume_entity created = _repository.create(response);
        return _mapper.toDto(created);
    }

    @Transactional
    public ResumeResponseDto update(UUID Id, ResumeRequestDto request) {
        Resume_entity resume = _repository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Резюме не найдено: ID - " + Id));
        _mapper.updateEntity(request, resume);
        Resume_entity updated = _repository.create(resume);
        return _mapper.toDto(updated);
    }

    @Transactional
    public void delete(UUID Id) {
        _repository.deleteById(Id);
    }
}
