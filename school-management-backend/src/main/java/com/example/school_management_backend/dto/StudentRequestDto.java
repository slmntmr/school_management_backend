package com.example.school_management_backend.dto;

import lombok.Builder;
import lombok.Data;

// DTO for creating a new student
@Builder
@Data
public class StudentRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String studentNumber;
    private Double grade;
}
