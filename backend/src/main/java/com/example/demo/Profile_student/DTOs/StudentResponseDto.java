package com.example.demo.Profile_student.DTOs;

import java.util.UUID;

public record StudentResponseDto(UUID User_ID,String Full_name,String Group,String Course,String Direction,String Phone,String Avatar_url,boolean Visibility) {
}
