package com.example.demo.Profile_student;

import com.example.demo.Profile_student.DTOs.StudentRequestDto;
import com.example.demo.Profile_student.DTOs.StudentResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StudentService {
    private final IStudentMapper _mapper;
    private final IStudentRepository _repository;

    public StudentService(IStudentMapper mapper, IStudentRepository repository) {
        _mapper = mapper;
        _repository = repository;
    }

    public List<StudentResponseDto> findAll()
    {
        return _mapper.toDtoList(_repository.findAll());
    }

    public Optional<StudentResponseDto> findById(UUID Id)
    {
        return _repository.findById(Id).map(_mapper::toDto);
    }

    public Optional<StudentResponseDto> findByName(String Name)     {
        return _repository.findByName(Name).map(_mapper::toDto);
    }

    @Transactional
    public StudentResponseDto create(StudentRequestDto request) {
        Student_entity response = _mapper.toEntity(request);
        Student_entity created = _repository.create(response);
        return _mapper.toDto(created);
    }

    @Transactional
    public StudentResponseDto update(UUID Id, StudentRequestDto request) {
        Student_entity student = _repository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден: ID - " + Id));
        _mapper.updateEntity(request, student);
        Student_entity updated = _repository.create(student);
        return _mapper.toDto(updated);
    }

    @Transactional
    public void delete(UUID Id) {
        _repository.deleteById(Id);
    }
}
