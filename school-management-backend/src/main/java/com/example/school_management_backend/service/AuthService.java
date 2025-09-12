package com.example.school_management_backend.service;

import com.example.school_management_backend.dto.AuthResponse;
import com.example.school_management_backend.dto.LoginRequest;
import com.example.school_management_backend.dto.RegisterRequest;
import com.example.school_management_backend.entity.User;
import com.example.school_management_backend.enums.Role;
import com.example.school_management_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Bu sınıf bir service olarak işaretleniyor ve Spring tarafından yönetiliyor
@Service
@RequiredArgsConstructor // Constructor injection için Lombok anotasyonu
public class AuthService {

    // Gerekli bağımlılıkları tanımlıyoruz
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Kullanıcıyı kayıt etme işlemi
    public AuthResponse register(RegisterRequest request) {
        // Eğer email zaten kayıtlıysa exception fırlat
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı!");
        }

        // Yeni kullanıcı nesnesi oluşturuluyor
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Şifreyi şifrele
                .role(request.getRole() != null ? request.getRole() : Role.OGRENCI) // Rol null gelirse OGRENCI olarak ata
                .build();

        // Kullanıcı veritabanına kaydediliyor
        userRepository.save(user);

        // JWT üretme işlemi sonra eklenecek, şimdilik sabit bir değer dönelim
        return AuthResponse.builder()
                .token("fake-jwt-token") // Gerçek token JwtUtil ile üretilecek
                .build();
    }

    // Kullanıcı giriş işlemi
    public AuthResponse login(LoginRequest request) {
        // E-mail ile kullanıcı aranıyor
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Şifre karşılaştırılıyor
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        // Token üretme işlemi sonra yapılacak
        return AuthResponse.builder()
                .token("fake-jwt-token") // Geçici olarak sabit token
                .build();
    }
}
