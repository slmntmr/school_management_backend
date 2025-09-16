package com.example.school_management_backend.mapper;

import com.example.school_management_backend.dto.StudentRequestDto;
import com.example.school_management_backend.dto.StudentResponseDto;
import com.example.school_management_backend.entity.Student; // <<-- UPDATED
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    // DTO -> Entity (uses Lombok @Builder on Student)
    public Student mapRequestToStudent(StudentRequestDto dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .studentNumber(dto.getStudentNumber())
                .grade(dto.getGrade())
                .build();
    }

    // Entity -> DTO
    public StudentResponseDto mapStudentToResponse(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentNumber(student.getStudentNumber())
                .grade(student.getGrade())
                .build();
    }
}
