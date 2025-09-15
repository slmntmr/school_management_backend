package com.example.school_management_backend.service;

import com.example.school_management_backend.dto.OgrenciRequestDTO;
import com.example.school_management_backend.dto.OgrenciResponseDTO;
import com.example.school_management_backend.entity.User;
import com.example.school_management_backend.enums.Role;
import com.example.school_management_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OgrenciService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Öğrenci kaydı
    public OgrenciResponseDTO registerOgrenci(OgrenciRequestDTO requestDTO) {
        // 1. Email zaten var mı kontrol et
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Bu email zaten kayıtlı");
        }

        // 2. Entity oluştur
        User user = User.builder()
                .fullName(requestDTO.getFullName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(Role.OGRENCI)
                .classroom(requestDTO.getClassroom())
                .schoolNumber(requestDTO.getSchoolNumber())
                .build();

        // 3. Kaydet
        User saved = userRepository.save(user);

        // 4. Response DTO dön
        return OgrenciResponseDTO.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .classroom(saved.getClassroom())
                .schoolNumber(saved.getSchoolNumber())
                .role(saved.getRole())
                .build();
    }

    // Profil bilgisini alma
    public OgrenciResponseDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı"));
        // Response DTO dön
        return OgrenciResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .classroom(user.getClassroom())
                .schoolNumber(user.getSchoolNumber())
                .role(user.getRole())
                .build();
    }

    // Profil güncelleme — kendi hesabı ya da admin/mudur yetkisiyle
    public OgrenciResponseDTO updateOgrenci(String email, OgrenciRequestDTO updateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı"));

        // İstenilen alanları güncelle
        user.setFullName(updateDTO.getFullName());
        // email güncellemesi ekstra kontrol gerekebilir
        user.setClassroom(updateDTO.getClassroom());
        user.setSchoolNumber(updateDTO.getSchoolNumber());
        // şifre güncellemesi varsa özel metod vs.

        User updated = userRepository.save(user);

        return OgrenciResponseDTO.builder()
                .id(updated.getId())
                .fullName(updated.getFullName())
                .email(updated.getEmail())
                .classroom(updated.getClassroom())
                .schoolNumber(updated.getSchoolNumber())
                .role(updated.getRole())
                .build();
    }

    // Diğer yöntemler: şifre değiştirme, silme vs.
}
