package com.example.school_management_backend.dto;

import lombok.Builder;
import lombok.Data;

// DTO for returning student info to client
@Builder
@Data
public class StudentResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentNumber;
    private Double grade;
}
