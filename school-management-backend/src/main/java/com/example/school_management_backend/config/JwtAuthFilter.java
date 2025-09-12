package com.example.school_management_backend.config;

import com.example.school_management_backend.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Bu filtre her istekte bir kez çalışır (OncePerRequestFilter)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // Token oluşturma ve çözümleme işlemleri
    private final CustomUserDetailsService userDetailsService; // DB'den kullanıcıyı getirme

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Header'dan Authorization değerini al
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;

        // Header 'Bearer ' ile başlıyorsa token’ı al
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // "Bearer " kısmını at
            try {
                userEmail = jwtUtil.extractUsername(jwt); // Email’i token’dan al
            } catch (ExpiredJwtException e) {
                System.out.println("Token süresi dolmuş: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Token geçersiz: " + e.getMessage());
            }
        }

        // Eğer email varsa ve kullanıcı zaten doğrulanmamışsa işlemleri yap
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Veritabanından kullanıcı bilgilerini al
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Token geçerliyse
            if (jwtUtil.isTokenValid(jwt, (com.example.school_management_backend.entity.User) userDetails)) {

                // Authentication objesi oluştur
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // İsteğe detay ekle (IP vs.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security’ye authentication objesini kaydet
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // İsteği zincirde bir sonraki filtreye gönder
        filterChain.doFilter(request, response);
    }
}
