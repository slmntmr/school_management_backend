package com.example.school_management_backend.service;

import com.example.school_management_backend.config.JwtUtil;
import com.example.school_management_backend.dto.AuthRequest;
import com.example.school_management_backend.dto.AuthResponse;
import com.example.school_management_backend.dto.RegisterRequest;
import com.example.school_management_backend.entity.User;
import com.example.school_management_backend.enums.Role;
import com.example.school_management_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // ✅ JWT token üreteceğimiz yardımcı sınıf

    // ✅ Kayıt işlemi
    public AuthResponse register(RegisterRequest request) {
        // Aynı email ile daha önce kayıt yapılmış mı kontrol ediyoruz
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı!");
        }

        // Yeni kullanıcıyı oluşturuyoruz
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.OGRENCI)
                .build();

        // Kullanıcıyı veritabanına kaydediyoruz
        userRepository.save(user);

        // ✅ JWT token üretiliyor
        String token = jwtUtil.generateToken(user);

        // Token'ı response içinde dönüyoruz
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    // ✅ Giriş işlemi
    public AuthResponse login(AuthRequest request) {
        // Kullanıcı email'ine göre aranıyor
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Şifre doğru mu kontrol ediliyor
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        // ✅ JWT token üretiliyor
        String token = jwtUtil.generateToken(user);

        // Token response ile dönülüyor
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
