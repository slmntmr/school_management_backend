package com.example.school_management_backend.controller;

import com.example.school_management_backend.dto.AuthRequest;
import com.example.school_management_backend.dto.AuthResponse;
import com.example.school_management_backend.dto.RegisterRequest;
import com.example.school_management_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // URL: /api/auth/register, /api/auth/login
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ✅ Kullanıcı kayıt endpoint'i
    @PostMapping("/register") //POST http://localhost:8080/api/auth/register
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Kullanıcı login endpoint'i
    @PostMapping("/login") //POST http://localhost:8080/api/auth/login
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
