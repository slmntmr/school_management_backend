package com.example.school_management_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String email;    // Giriş için e-posta
    private String password; // Giriş için şifre
}
