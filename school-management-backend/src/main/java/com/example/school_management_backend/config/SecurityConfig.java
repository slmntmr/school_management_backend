package com.example.school_management_backend.config;

import com.example.school_management_backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity  // @PreAuthorize gibi annotation'lar için gerekli
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF konfigürasyonu: disable etme işlemi yeni DSL ile
                .csrf(csrf -> csrf.disable())

                // Session yönetimi, stateless olmalı çünkü JWT kullanıyoruz
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Yetkilendirme: hangi endpoint kimlere açık / kimler authenticated olmalı
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Örnek roller ile endpoint sınıflandırması
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/mudur/**").hasAnyRole("MUDUR","ADMIN")
                        .requestMatchers("/api/ogretmen/**").hasAnyRole("OGRETMEN","MUDUR","ADMIN")
                        .requestMatchers("/api/ogrenci/**").hasAnyRole("OGRENCI","OGRETMEN","MUDUR","ADMIN")
                        .anyRequest().authenticated()
                )

                // JWT filtresi: UsernamePassword filtresinden önce
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
