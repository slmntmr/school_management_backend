package com.example.school_management_backend.config;

import com.example.school_management_backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Bu sınıf güvenlik ayarlarını içerir
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // JWT filtre sınıfı (bir sonraki adımda yazacağız)
    private final CustomUserDetailsService userDetailsService; // Kullanıcıyı DB’den çeken servis (bir sonraki adımda yazacağız)

    // Şifreleme bean’i: BCrypt algoritması
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean’i: login işlemi için gerekli
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Ana güvenlik ayarlarını içeren filtre zinciri
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF kapatıldı (token kullandığımız için gerek yok)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Giriş/kayıt endpoint’leri herkese açık
                        .anyRequest().authenticated() // Diğer tüm istekler giriş gerektirir
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Her istekte yeni token kontrolü yapılır
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT filtresini ekle

        return http.build();
    }
    /*Bu sınıf sayesinde:

JWT token'lı kullanıcılar doğrulanacak

Yetkisiz erişimler engellenecek

Şifreler encode edilecek

Rollere göre sayfalara erişim kontrolü yapılacak*/



    /*Özellik	Açıklama
csrf().disable()	CSRF koruması kapatılır çünkü token tabanlı sistemde gerek yok.
/api/auth/**	Giriş ve kayıt endpoint'leri herkese açık olacak.
sessionCreationPolicy(STATELESS)	Sunucu oturum tutmaz, her istek token ile doğrulanır.
addFilterBefore()	JWT filtremiz, Spring Security'nin default username/password filtresinden önce çalışacak.*/
}
