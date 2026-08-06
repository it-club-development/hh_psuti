package com.example.demo.auth.controller;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.model.CompanyEntity;
import com.example.demo.auth.model.StudentEntity;
import com.example.demo.auth.repository.CompanyRepository;
import com.example.demo.auth.repository.StudentRepository;
import com.example.demo.auth.security.JWT_util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository employerRepository;

    @Autowired
    private JWT_util jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register/student")
    public ResponseEntity<AuthResponse> registerStudent(@RequestBody StudentRegisterRequest request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Email already exists"));
        }

        StudentEntity student = new StudentEntity();
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setName(request.getName());
        student.setUniversity(request.getUniversity());
        student.setFaculty(request.getFaculty());
        student.setCourse(request.getCourse());
        student.setSpecialty(request.getSpecialty());
        student.setBirthDate(request.getBirthDate());
        student.setPhoneNumber(request.getPhoneNumber());

        studentRepository.save(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse("Student registered successfully"));
    }

    @PostMapping("/register/employer")
    public ResponseEntity<AuthResponse> registerEmployer(@RequestBody CompanyRegisterRequest request) {
        if (employerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Email already exists"));
        }

        CompanyEntity employer = new CompanyEntity();
        employer.setEmail(request.getEmail());
        employer.setPassword(passwordEncoder.encode(request.getPassword()));
        employer.setName(request.getName());
        employer.setCompanyName(request.getCompanyName());
        employer.setPosition(request.getPosition());
        employer.setDepartment(request.getDepartment());
        employer.setCompanyPhone(request.getCompanyPhone());
        employer.setCompanyAddress(request.getCompanyAddress());
        employer.setWebsite(request.getWebsite());

        employerRepository.save(employer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse("Employer registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Проверяем среди студентов
        StudentEntity student = studentRepository.findByEmail(request.getEmail()).orElse(null);
        if (student != null && passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            String token = jwtUtil.generateToken(student.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, student.getEmail()));
        }

        // Проверяем среди работодателей
        CompanyEntity employer = employerRepository.findByEmail(request.getEmail()).orElse(null);
        if (employer != null && passwordEncoder.matches(request.getPassword(), employer.getPassword())) {
            String token = jwtUtil.generateToken(employer.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, employer.getEmail()));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse("Invalid email or password"));
    }
}