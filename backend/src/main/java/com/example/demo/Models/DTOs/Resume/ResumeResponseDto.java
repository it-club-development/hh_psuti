package com.example.demo.Models.DTOs.Resume;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResumeResponseDto( UUID ID, UUID Student_ID, String Skills,String Portfolio_links,String Grades_comment, LocalDateTime Created_at, LocalDateTime Updated_at) {
}
