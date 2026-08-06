package com.example.demo.auth.dto;

import java.time.LocalDate;

public class StudentRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String university;
    private String faculty;
    private Integer course;
    private String specialty;
    private LocalDate birthDate;
    private String phoneNumber;

    // приём и отдача
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public Integer getCourse() { return course; }
    public void setCourse(Integer course) { this.course = course; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
