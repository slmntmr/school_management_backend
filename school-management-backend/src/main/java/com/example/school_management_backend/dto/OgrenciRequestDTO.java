package com.example.school_management_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Öğrenci kayıt ya da güncelleme isteği için kullanılacak DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OgrenciRequestDTO {
    private String fullName;
    private String email;
    private String password;
    // Öğrenciye özgü bilgi varsa (sınıf, numara vs)
    private String classroom;
    private String schoolNumber;
}
