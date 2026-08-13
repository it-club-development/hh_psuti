package com.example.demo.auth.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import java.time.LocalDate;
@Entity
@Table(name = "students")
public class StudentEntity extends user {
    private String Name;
    private String university;
    private String faculty;
    private Integer course;
    private String specialty;
    private LocalDate birthDate;
    private String phoneNumber;

    // приём и отдача
    public String getName() {return Name;}
    public void setName(String Name) {this.Name = Name;}
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
