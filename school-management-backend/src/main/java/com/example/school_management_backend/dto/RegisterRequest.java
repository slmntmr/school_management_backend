package com.example.school_management_backend.dto;

import com.example.school_management_backend.enums.Role;
import lombok.*;

// Lombok anotasyonları ile getter/setter ve constructorları otomatik oluşturuyoruz
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String fullName; // Kullanıcı adı soyadı
    private String email;    // E-posta
    private String password; // Şifre
    private Role role;       // Kullanıcı rolü (ADMIN, MUDUR, OGRETMEN, OGRENCI)
}
