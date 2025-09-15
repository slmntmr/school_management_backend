package com.example.school_management_backend.entity;

import com.example.school_management_backend.enums.Role; // ENUM BURADAN GELİYOR
import jakarta.persistence.*;
import lombok.*;

// Lombok ile getter, setter, constructor vs. otomatik tanımlanır
@Data // Getter & Setter
@NoArgsConstructor // Parametresiz constructor
@AllArgsConstructor // Tüm alanları içeren constructor
@Builder // Builder deseni ile nesne oluşturmak için
@Entity // Bu sınıf bir JPA Entity’sidir (veritabanı tablosudur)
@Table(name = "users") // Veritabanında tablonun adı "users" olacak
public class User {

    @Id // Bu alan primary key olacak
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan ID
    private Long id;

    @Column(nullable = false) // Boş bırakılamaz
    private String fullName;

    @Column(unique = true, nullable = false) // E-posta hem benzersiz hem boş olamaz
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Enum değerini string olarak sakla (ADMIN, OGRENCI gibi)
    @Column(nullable = false)
    private Role role;


    // Eğer öğrenciye özel başka alanlar varsa örneğin sınıf, bölümü vs:
    private String classroom;
    private String schoolNumber;

    public boolean isEnabled() {
        return false;
    }
}
