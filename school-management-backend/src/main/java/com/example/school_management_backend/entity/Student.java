package com.example.school_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // <-- bunu ekle
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment ID
    private Long id;

    @Column(nullable = false)
    private String firstName; // Student first name

    @Column(nullable = false)
    private String lastName; // Student last name

    @Column(unique = true, nullable = false)
    private String email; // Student email (must be unique)

    private String phone; // Student phone

    @Column(unique = true, nullable = false)
    private String studentNumber; // Student number (unique ID for each student)

    private Double grade; // Student overall grade (example field)
}
