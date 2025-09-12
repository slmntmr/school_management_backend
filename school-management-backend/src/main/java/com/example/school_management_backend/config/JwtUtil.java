package com.example.school_management_backend.config;

import com.example.school_management_backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Bu sınıf token oluşturma ve çözümleme işlemlerini yapar
@Component
public class JwtUtil {

    // Token’ı imzalamak için gizli bir anahtar (256 bit güvenli)
    private final String SECRET_KEY = "buCokGucluBirJwtAnahtariOlmaliVeEnAz256bitOlmali!!!";

    // Anahtarı byte dizisi olarak alıp JJWT kütüphanesine uygun key nesnesine dönüştürür
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Token oluşturma işlemi
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name()); // Token içine rolü de ekliyoruz
        return createToken(claims, user.getEmail()); // Email subject olarak atanıyor
    }

    // Token detaylarıyla birlikte oluşturulur
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationTime = 1000 * 60 * 60 * 10; // 10 saat geçerli

        return Jwts.builder()
                .setClaims(claims) // Ek bilgiler (rol vs.)
                .setSubject(subject) // Genellikle kullanıcı emaili olur
                .setIssuedAt(new Date(System.currentTimeMillis())) // Şu anki zaman
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Bitiş zamanı
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // İmza ve algoritma
                .compact(); // Token string olarak döner
    }

    // Token içinden username (email) alma
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // subject = email
    }

    // Token geçerliliğini kontrol etme
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    // Token’ın süresi dolmuş mu?
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token’dan expiration bilgisi alma
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Token’dan claim (bilgi) çıkartma — generic
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Token çözümleme (secret key ile imzayı kontrol eder)
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*Metot	Açıklama
generateToken(User user)	Kullanıcıya özel bir JWT üretir.
extractUsername(token)	Token’dan email bilgisi (subject) çıkarılır.
isTokenValid(token, user)	Token’ın süresi dolmuş mu, kullanıcıya ait mi kontrol eder.
extractAllClaims(token)	Token içindeki tüm bilgileri (claim) çözümler.*/
}
