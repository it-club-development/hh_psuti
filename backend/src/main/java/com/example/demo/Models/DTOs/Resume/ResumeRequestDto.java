package com.example.demo.Models.DTOs.Resume;

import java.util.UUID;

public record ResumeRequestDto(UUID Student_ID, String Skills, String Portfolio_links, String Grades_comment) {
}
