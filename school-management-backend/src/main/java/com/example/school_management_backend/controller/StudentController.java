package com.example.school_management_backend.controller;

import com.example.school_management_backend.dto.StudentRequestDto;
import com.example.school_management_backend.dto.StudentResponseDto;
import com.example.school_management_backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Endpoint for creating a new student
    // Only ADMIN and MUDUR can add students
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MUDUR')")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequestDto dto) {
        try {
            StudentResponseDto response = studentService.createStudent(dto);
            return ResponseEntity.ok(response); // return created student info
        } catch (RuntimeException e) {
            // Return error message if duplicate email or studentNumber
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
