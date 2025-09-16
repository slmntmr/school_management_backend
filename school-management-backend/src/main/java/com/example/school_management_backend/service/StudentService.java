package com.example.school_management_backend.service;

import com.example.school_management_backend.dto.StudentRequestDto;
import com.example.school_management_backend.dto.StudentResponseDto;

import com.example.school_management_backend.entity.Student;
import com.example.school_management_backend.mapper.StudentMapper;
import com.example.school_management_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    public StudentResponseDto createStudent(StudentRequestDto dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("A student with this email already exists!");
        }
        if (studentRepository.existsByStudentNumber(dto.getStudentNumber())) {
            throw new RuntimeException("A student with this student number already exists!");
        }

        // Mapping işini Mapper yapıyor
        Student student = studentMapper.mapRequestToStudent(dto);
        Student saved = studentRepository.save(student);

        return studentMapper.mapStudentToResponse(saved);
    }
}
