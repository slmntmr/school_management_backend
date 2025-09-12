package com.example.school_management_backend.service;

import com.example.school_management_backend.entity.User;
import com.example.school_management_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security'nin kullanıcıyı veritabanından yüklemesi için özel servis
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Email'e göre kullanıcıyı yükler (JWT token'dan alınan email ile çağrılır)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // E-mail'e göre kullanıcıyı veritabanından çek
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        // User entity’sini Spring Security'nin anlayacağı UserDetails nesnesine dönüştür
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())                 // Kullanıcı adı olarak email
                .password(user.getPassword())             // Şifre (BCrypt ile encode edilmiş)
                .roles(user.getRole().name())             // Rol (Spring role bazlı güvenlikte kullanır)
                .build();
    }
}
