package com.example.school_management_backend.repository;


import com.example.school_management_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for accessing Student table
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    boolean existsByStudentNumber(String studentNumber);
}
