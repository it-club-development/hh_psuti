package com.example.demo.Services;

import com.example.demo.Models.DTOs.Response.ResponseRequestDto;
import com.example.demo.Models.DTOs.Response.ResponseResponseDto;
import com.example.demo.Models.Mappers.IResponseMapper;
import com.example.demo.Models.Response_entity;
import com.example.demo.Repositories.IResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ResponseService {
    private final IResponseRepository _repository;
    private final IResponseMapper _mapper;

    ResponseService(IResponseMapper mapper, IResponseRepository repository)
    {
        _mapper = mapper;
        _repository = repository;
    }

    public List<ResponseResponseDto> findAll()
    {
        return _mapper.toDtoList(_repository.findAll());
    }

    public Optional<ResponseResponseDto> findById(UUID Id)
    {
        return _repository.findById(Id).map(_mapper::toDto);
    }


    @Transactional
    public ResponseResponseDto create(ResponseRequestDto request) {
        Response_entity response = _mapper.toEntity(request);
        Response_entity created = _repository.create(response);
        return _mapper.toDto(created);
    }

    @Transactional
    public ResponseResponseDto update(UUID Id, ResponseRequestDto request) {
        Response_entity response = _repository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Отклик от компании не найден: ID - " + Id));
        _mapper.updateEntity(request, response);
        Response_entity updated = _repository.create(response);
        return _mapper.toDto(updated);
    }

    @Transactional
    public void delete(UUID Id) {
        _repository.deleteById(Id);
    }
}

